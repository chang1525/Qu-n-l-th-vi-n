package com.library.strategy;

public class BookFineStrategy implements FineStrategy {

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * 5000.0;   // Sách phạt 5.000đ/ngày
    }
}