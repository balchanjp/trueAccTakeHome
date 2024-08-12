package com.testing.trueaccord.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.testing.trueaccord.communicator.PaymentCommunicator;
import com.testing.trueaccord.model.Debt;
import com.testing.trueaccord.model.Payment;
import com.testing.trueaccord.model.PaymentPlan;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.testing.trueaccord.constants.PaymentConstants.BI_WEEKLY;
import static com.testing.trueaccord.constants.PaymentConstants.WEEKLY;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DebtServiceImplTest {
    @InjectMocks
    DebtServiceImpl debtService;
    @Mock
    private PaymentCommunicator paymentCommunicator;
    private List<Debt> debtsList;
    private List<Payment> paymentList;
    private List<PaymentPlan> planList;

    private static final double DELTA = 0.001;


    @Before
    public void setup() throws Exception{
        Debt debt1 = new Debt();
        debt1.setId(0);
        debt1.setAmount(50.0);
        Debt debt2 = new Debt();
        debt2.setId(1);
        debt2.setAmount(100.5);
        Debt debt3 = new Debt();
        debt3.setId(2);
        debt3.setAmount(88.8);
        debtsList =  Arrays.asList(debt1, debt2, debt3);
        when(paymentCommunicator.getDebts()).thenReturn(debtsList);
        Payment payment1 = new Payment();
        payment1.setPayment_plan_id(0);
        payment1.setAmount(15.0);
        payment1.setDate(new Date(50000));
        Payment payment2 = new Payment();
        payment2.setPayment_plan_id(0);
        payment2.setAmount(15.0);
        payment2.setDate(new Date(50000 + TimeUnit.DAYS.toMillis(15)));
        Payment payment3 = new Payment();
        payment3.setPayment_plan_id(1);
        payment3.setAmount(32.0);
        payment3.setDate(new Date(60000));
        Payment payment4 = new Payment();
        payment4.setPayment_plan_id(1);
        payment4.setAmount(32.0);
        payment4.setDate(new Date(60000 + TimeUnit.DAYS.toMillis(8)));
        Payment payment5 = new Payment();
        payment5.setPayment_plan_id(1);
        payment5.setAmount(32.0);
        payment5.setDate(new Date(6000 + TimeUnit.DAYS.toMillis(8)));
        paymentList = Arrays.asList(payment1, payment2, payment3, payment4, payment5);
        when(paymentCommunicator.getPayments()).thenReturn(paymentList);
        PaymentPlan paymentPlan0 = new PaymentPlan();
        paymentPlan0.setAmount_to_pay(45.0);
        paymentPlan0.setDebt_id(0);
        paymentPlan0.setId(0);
        paymentPlan0.setInstallment_amount(15.0);
        paymentPlan0.setStart_date(new Date(50000));
        paymentPlan0.setInstallment_frequency(BI_WEEKLY);
        PaymentPlan paymentPlan1 = new PaymentPlan();
        paymentPlan1.setAmount_to_pay(96.0);
        paymentPlan1.setId(1);
        paymentPlan1.setDebt_id(1);
        paymentPlan1.setInstallment_amount(32.0);
        paymentPlan1.setStart_date(new Date(60000));
        paymentPlan1.setInstallment_frequency(WEEKLY);
        planList = Arrays.asList(paymentPlan0, paymentPlan1);
        when(paymentCommunicator.getPlans()).thenReturn(planList);
    }

    @Test(expected = JsonProcessingException.class)
    public void getDebtData_CommunicatorExceptionTest() throws JsonProcessingException {
        when(paymentCommunicator.getDebts()).thenThrow(mock(JsonProcessingException.class));
        debtService.getDebtData();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDebtData_InvalidInstallmentFrequencyTest() throws JsonProcessingException {
        planList.get(0).setInstallment_frequency("blah");
        debtService.getDebtData();
    }

    @Test
    public void getDebtData_SuccessTest() throws JsonProcessingException {
        List<Debt> actualData = debtService.getDebtData();

        assertNotNull(actualData);
        Debt debt = actualData.get(0);
        assertEquals(0, debt.getId());
        assertEquals(50.0, debt.getAmount(), DELTA);
        assertEquals(15.0, debt.getRemaining_amount(), DELTA);
        assertTrue(debt.is_in_payment_plan());
        assertEquals((new Date(50000 + TimeUnit.DAYS.toMillis(28))), (debt.getNext_payment_due_date()));

        Debt debt1 = actualData.get(1);
        assertEquals(1, debt1.getId());
        assertEquals(100.5, debt1.getAmount(), DELTA);
        assertEquals(0.0, debt1.getRemaining_amount(), DELTA);
        assertFalse(debt1.is_in_payment_plan());
        assertNull(debt1.getNext_payment_due_date());

        Debt debt2 = actualData.get(2);
        assertEquals(2, debt2.getId());
        assertEquals(88.8, debt2.getAmount(), DELTA);
        assertEquals(88.8, debt2.getRemaining_amount(), DELTA);
        assertFalse(debt2.is_in_payment_plan());
        assertNull(debt2.getNext_payment_due_date());
    }
}
