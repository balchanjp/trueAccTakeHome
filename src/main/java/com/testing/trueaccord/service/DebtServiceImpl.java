package com.testing.trueaccord.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.testing.trueaccord.communicator.PaymentCommunicator;
import com.testing.trueaccord.model.Debt;
import com.testing.trueaccord.model.Payment;
import com.testing.trueaccord.model.PaymentPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.testing.trueaccord.constants.PaymentConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DebtServiceImpl implements DebtService {


    private final PaymentCommunicator paymentCommunicator;

    @Override
    public List<Debt> getDebtData() throws JsonProcessingException, IllegalArgumentException {
        List<Debt> debts = null;
        List<Payment> payments = null;
        List<PaymentPlan> paymentPlans = null;
        try {
            debts = paymentCommunicator.getDebts();
            payments = paymentCommunicator.getPayments();
            paymentPlans = paymentCommunicator.getPlans();
        } catch (Exception e){
            log.debug("Exception getting data", e);
            throw e;
        }
        processData(debts, paymentPlans, payments);
        printDebts(debts);
        return debts;
    }


    private void processData(List<Debt> debts, List<PaymentPlan> plans, List<Payment> payments) {
        for(Debt debt : debts){
            Optional<PaymentPlan> plan = plans.stream().filter(p -> p.getDebt_id() == debt.getId()).findFirst();
            if(!plan.isEmpty()) {
                List<Payment> relatedPayments = payments.stream().filter(payment -> payment.getPayment_plan_id() == plan.get().getId()).collect(Collectors.toList());
                assignAmountToPay(debt, plan.get().getAmount_to_pay(), relatedPayments.stream());
                if(debt.getRemaining_amount() > 0) {
                    debt.set_in_payment_plan(true);
                    assignPaymentDate(debt, plan.get(), relatedPayments.stream());
                } else {
                    debt.set_in_payment_plan(false);
                }
            } else {
                debt.setRemaining_amount(debt.getAmount());
                debt.set_in_payment_plan(false);
            }
        }
    }

    private void assignPaymentDate(Debt debt, PaymentPlan plan, Stream<Payment> payments) {
        debt.setNext_payment_due_date(calculateNextPaymentDate(plan, payments));
    }

    private void assignAmountToPay(Debt debt, double amountToPay, Stream<Payment> payments) {
        debt.setRemaining_amount(calculateRemainingDebt(amountToPay, payments));
    }

    private double calculateRemainingDebt(double amountRemaining, Stream<Payment> payments) {
        double amountPaid = payments.mapToDouble(Payment::getAmount).reduce(0, (x, y) -> x + y);
        return amountRemaining - amountPaid;
    }

    private Date calculateNextPaymentDate(PaymentPlan plan, Stream<Payment> payments){
        Date currentDate = plan.getStart_date();
        long iteration = MILLIS_IN_A_WEEK;
        if(BI_WEEKLY.equals(plan.getInstallment_frequency())){
            iteration *= 2;
        } else if (!plan.getInstallment_frequency().equals(WEEKLY)){
            throw new IllegalArgumentException("Invalid installment frequency");
        }
        Date lastPayment = payments.map(payment -> payment.getDate()).reduce(new Date(0), (x, y) -> x.getTime() > y.getTime() ? x : y);
        while(currentDate.getTime() < lastPayment.getTime()){
            currentDate.setTime(currentDate.getTime() + iteration);
        }
        return currentDate;
    }

    private void printDebts(List<Debt> debts) {
        for (Debt debt : debts){
            System.out.println(debt.toString());
        }
    }
}