package com.testing.trueaccord.communicator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.testing.trueaccord.model.Debt;
import com.testing.trueaccord.model.Payment;
import com.testing.trueaccord.model.PaymentPlan;

import java.io.IOException;
import java.util.List;

public interface PaymentCommunicator {

    List<Debt> getDebts() throws JsonProcessingException;

    List<PaymentPlan> getPlans() throws JsonProcessingException;

    List<Payment> getPayments() throws JsonProcessingException;


}
