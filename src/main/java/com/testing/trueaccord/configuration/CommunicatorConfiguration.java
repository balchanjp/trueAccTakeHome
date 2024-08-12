package com.testing.trueaccord.configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Slf4j
public class CommunicatorConfiguration {

    @Value("${communicator.debtsUri:}")
    private String debtsUri;
    @Value("${communicator.paymentsUri}")
    private String paymentsUri;
    @Value("${communicator.paymentPlansUri}")
    private String paymentPlansUri;

}
