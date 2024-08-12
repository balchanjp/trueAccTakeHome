package com.testing.trueaccord.communicator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.trueaccord.model.Debt;
import com.testing.trueaccord.model.Payment;
import com.testing.trueaccord.model.PaymentPlan;
import com.testing.trueaccord.configuration.CommunicatorConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentCommunicatorImpl implements PaymentCommunicator {

    @Autowired
    private CommunicatorConfiguration communicatorConfiguration;
    @Autowired
    private HttpClient httpClient;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Debt> getDebts() throws JsonProcessingException {
        HttpRequest request = generateRequest(communicatorConfiguration.getDebtsUri());
        HttpResponse<String> response = getResponse(request);
        return response == null ? null : objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Debt.class));
    }

    @Override
    public List<PaymentPlan> getPlans() throws JsonProcessingException {
        HttpRequest request = generateRequest(communicatorConfiguration.getPaymentPlansUri());
        HttpResponse<String> response = getResponse(request);
        return response == null ? null : objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, PaymentPlan.class));
    }

    @Override
    public List<Payment> getPayments() throws JsonProcessingException {
        HttpRequest request = generateRequest(communicatorConfiguration.getPaymentsUri());
        HttpResponse<String> response = getResponse(request);
        return response == null ? null : objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Payment.class));
    }


    private HttpRequest generateRequest(String uri){
        return HttpRequest.newBuilder(
                URI.create(uri))
                .header("Accept", "application/json")
                .build();
    }

    private HttpResponse<String> getResponse(HttpRequest request){
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.debug("Failed to send GET request: " + e.getMessage());
        }
        return response;
    }


}
