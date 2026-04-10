package com.library.entity;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {
    @Override
    public double calculateFine(int daysLate) {
        return 0; // Admin doesn't pay fines
    }
}
