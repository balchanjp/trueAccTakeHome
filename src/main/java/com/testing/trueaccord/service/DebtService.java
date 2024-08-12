package com.testing.trueaccord.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.testing.trueaccord.model.Debt;

import java.util.List;

public interface DebtService {
    List<Debt> getDebtData() throws JsonProcessingException, IllegalArgumentException;
}
