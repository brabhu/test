package com.test.payment.model;

public enum SubscriptionType {
    DAILY("DAILY"),
    WEEKLY("WEEKLY"),
    MONTHLY("MONTHLY");


    private String text;

    SubscriptionType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static SubscriptionType fromString(String text) {
        for (SubscriptionType subscriptionType : SubscriptionType.values()) {
            if (subscriptionType.text.equalsIgnoreCase(text)) {
                return subscriptionType;
            }
        }
        return null;
    }
}
