package com.library.strategy;

public class JournalFineStrategy implements FineStrategy {

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * 3000.0;   // Tạp chí khoa học phạt 3.000đ/ngày
    }
}