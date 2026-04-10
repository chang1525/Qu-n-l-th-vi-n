package com.library.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Book extends Document {

    private String isbn;

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * 5000.0; // Sách hiếm phạt cao
    }
}