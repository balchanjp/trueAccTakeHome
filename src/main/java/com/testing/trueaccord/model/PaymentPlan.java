package com.testing.trueaccord.model;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentPlan {
    private int id;
    private int debt_id;
    private double amount_to_pay;
    private String installment_frequency;
    private double installment_amount;
    private Date start_date;
}
