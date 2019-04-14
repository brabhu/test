package com.test.payment.model;

import java.util.Date;
import java.util.List;

public class Payment {
    String id;
    Amount amount;
    SubscriptionType subscription_type;
    List<Date> invoice_dates;

    public Payment(String id, Amount amount, SubscriptionType subscription_type, List<Date> invoice_dates) {
        this.id = id;
        this.amount = amount;
        this.subscription_type = subscription_type;
        this.invoice_dates = invoice_dates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public SubscriptionType getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(SubscriptionType subscription_type) {
        this.subscription_type = subscription_type;
    }

    public List<Date> getInvoice_dates() {
        return invoice_dates;
    }

    public void setInvoice_dates(List<Date> invoice_dates) {
        this.invoice_dates = invoice_dates;
    }
}
