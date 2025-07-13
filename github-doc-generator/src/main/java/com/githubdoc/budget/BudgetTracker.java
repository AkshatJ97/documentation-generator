package com.githubdoc.budget;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BudgetTracker {
    private double budget;
    private double currentSpend = 0.0;

    public BudgetTracker(@Value("${budget.limit}") double budget) {
        this.budget = budget;
    }

    public synchronized boolean canSpend(double amount) {
        return (currentSpend + amount) <= budget;
    }

    public synchronized void spend(double amount) {
        currentSpend += amount;
    }

    public synchronized double getCurrentSpend() {
        return currentSpend;
    }

    public synchronized double getRemainingBudget() {
        return budget - currentSpend;
    }
}
