package com.testing.trueaccord.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.testing.trueaccord.model.Debt;
import com.testing.trueaccord.service.DebtService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationControllerTest {
    @InjectMocks
    ApplicationController applicationController;
    @Mock
    DebtService debtService;

    @Test
    public void retrieveDebts_Success() throws JsonProcessingException {
        when(debtService.getDebtData()).thenReturn(new ArrayList<>());
        List<Debt> emptyResponse = applicationController.retrieveDebts();
        assertNotNull(emptyResponse);
    }

    @Test
    public void retreiveDebts_Exception() throws JsonProcessingException {
        when(debtService.getDebtData()).thenThrow( new IllegalArgumentException("tada"));
        List<Debt> nullResponse = applicationController.retrieveDebts();
        assertNull(nullResponse);
    }
}
