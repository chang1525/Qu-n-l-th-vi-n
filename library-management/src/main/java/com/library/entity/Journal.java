package com.library.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Journal extends Document {

    private String volume;

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * 3000.0;
    }
}