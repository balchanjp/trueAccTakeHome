package com.testing.trueaccord.configuration;

import com.testing.trueaccord.communicator.PaymentCommunicator;
import com.testing.trueaccord.communicator.PaymentCommunicatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class ApplicationConfiguration {


    @Bean
    HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }
    @Bean
    public PaymentCommunicator paymentCommunicator() { return new PaymentCommunicatorImpl(); }

}
