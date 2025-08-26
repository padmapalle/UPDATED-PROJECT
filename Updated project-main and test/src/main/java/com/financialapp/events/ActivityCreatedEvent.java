package com.financialapp.events;

import org.springframework.context.ApplicationEvent;

import com.financialapp.entity.FinancialActivity;

public class ActivityCreatedEvent extends ApplicationEvent {
    private final FinancialActivity activity;

    public ActivityCreatedEvent(Object source, FinancialActivity activity) {
        super(source);
        this.activity = activity;
    }

    public FinancialActivity getActivity() {
        return activity;
    }
}
