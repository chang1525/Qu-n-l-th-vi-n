package com.library.strategy;

public class MagazineFineStrategy implements FineStrategy {

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * 2000.0;   // Tạp chí phạt 2.000đ/ngày
    }
}