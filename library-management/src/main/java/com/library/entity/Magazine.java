package com.library.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Magazine extends Document {

    private String issueNumber;

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * 2000.0;
    }
}