package com.testing.trueaccord.model;

import lombok.Data;

import java.util.Date;

@Data
public class Payment {

    private int payment_plan_id;
    private double amount;
    private Date date;
}
