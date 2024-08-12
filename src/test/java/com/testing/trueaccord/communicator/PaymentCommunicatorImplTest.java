package com.testing.trueaccord.communicator;

import com.testing.trueaccord.configuration.CommunicatorConfiguration;
import com.testing.trueaccord.model.Debt;
import com.testing.trueaccord.model.Payment;
import com.testing.trueaccord.model.PaymentPlan;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PaymentCommunicatorImplTest {
    @InjectMocks
    PaymentCommunicatorImpl paymentCommunicator;
    @Mock
    CommunicatorConfiguration communicatorConfiguration;
    @Mock
    HttpClient httpClient;
    @Mock
    HttpResponse httpResponse;

    @Before
    public void setup(){
        when(communicatorConfiguration.getDebtsUri()).thenReturn("http://www.this.com");
        when(communicatorConfiguration.getPaymentPlansUri()).thenReturn("http://www.that.com");
        when(communicatorConfiguration.getPaymentsUri()).thenReturn("http://www.theOther.com");
    }

    @Test
    public void getResponse_IOExceptionTest() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new IOException());
        List<Debt> actualResponse = paymentCommunicator.getDebts();
        assertNull(actualResponse);
    }

    @Test
    public void getResponse_InterruptedExceptionTest() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new InterruptedException());
        List<Payment> actualResponse = paymentCommunicator.getPayments();
        assertNull(actualResponse);
    }

    @Test
    public void getDebts_SuccessTest() throws IOException, InterruptedException {
        when(httpResponse.body()).thenReturn("[{\n" +
                "  \"id\": 0,\n" +
                "  \"amount\": 123.46\n" +
                "}]");
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        List<Debt> actualResponse = paymentCommunicator.getDebts();
        assertFalse(actualResponse.isEmpty());
        assertEquals(0, actualResponse.get(0).getId());
    }

    @Test
    public void getPayments_SuccessTest() throws IOException, InterruptedException {
        when(httpResponse.body()).thenReturn("[{\n" +
                "  \"payment_plan_id\": 0,\n" +
                "  \"amount\": 51.25,\n" +
                "  \"date\": \"2020-09-29\"\n" +
                "}]");
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        List<Payment> actualResponse = paymentCommunicator.getPayments();
        assertFalse(actualResponse.isEmpty());
        assertEquals(0, actualResponse.get(0).getPayment_plan_id());
    }

    @Test
    public void getPaymentPlans_SuccessTest() throws IOException, InterruptedException {
        when(httpResponse.body()).thenReturn("[{\n" +
                "  \"id\": 0,\n" +
                "  \"debt_id\": 0,\n" +
                "  \"amount_to_pay\": 102.50,\n" +
                "  \"installment_frequency\": \"WEEKLY\", \n" +
                "  \"installment_amount\": 51.25,\n" +
                "  \"start_date\": \"2020-09-28\"\n" +
                "}]");
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        List<PaymentPlan> actualResponse = paymentCommunicator.getPlans();
        assertFalse(actualResponse.isEmpty());
        assertEquals(0, actualResponse.get(0).getId());
        assertEquals(0, actualResponse.get(0).getDebt_id());
    }

}
