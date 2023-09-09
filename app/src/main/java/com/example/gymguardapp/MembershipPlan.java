package com.example.gymguardapp;

public class MembershipPlan {
    private String name;
    private String monthly;
    private String yearly;
    private String details;
    private String expiry;

    public MembershipPlan(String name, String monthly, String yearly, String details, String expiry) {
        this.name = name;
        this.monthly = monthly;
        this.yearly = yearly;
        this.details = details;
        this.expiry = expiry;
    }

    public String getName() {
        return name;
    }

    public String getMonthly() {
        return monthly;
    }

    public String getYearly() {
        return yearly;
    }

    public String getDetails() {
        return details;
    }

    public String getExpiry() {
        return expiry;
    }
}
