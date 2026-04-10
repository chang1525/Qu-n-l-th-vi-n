package com.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class Member extends User {
    private int maxBorrowLimit = 5;
    private int currentBorrowCount = 0;
    @Override
    public double calculateFine(int daysLate) {
        return daysLate * 10000.0; // Độc giả thường
    }
}