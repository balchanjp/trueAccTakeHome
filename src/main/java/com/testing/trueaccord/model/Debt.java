package com.testing.trueaccord.model;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
public class Debt {
    private int id;
    private double amount;
    private boolean is_in_payment_plan;
    private double remaining_amount;
    private Date next_payment_due_date;
    // I really dont like naming things like this in java instead of camelCase and would never do so normally

    @Override
    public String toString() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(tz);
        return "{\n\tid : " + id +
                ",\n\t amount : " + amount +
                ",\n\t is_in_payment_plan : " + is_in_payment_plan +
                ",\n\t remaining_amount : " + remaining_amount +
                ",\n\t next_payment_due_date : " + (next_payment_due_date != null ? df.format(next_payment_due_date) : "null") +
                "\n}";
    }
}
