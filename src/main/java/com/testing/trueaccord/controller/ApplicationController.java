package com.testing.trueaccord.controller;

import com.testing.trueaccord.model.Debt;
import com.testing.trueaccord.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    private DebtService debtService;

    @GetMapping(value="/debts")
    public @ResponseBody List<Debt> retrieveDebts(){
        List<Debt> response = null;
        try {
            response = debtService.getDebtData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
