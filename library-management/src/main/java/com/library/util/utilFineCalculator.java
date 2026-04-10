package com.library.util;

import com.library.entity.Document;
import com.library.strategy.BookFineStrategy;
import com.library.strategy.FineStrategy;
import com.library.strategy.JournalFineStrategy;
import com.library.strategy.MagazineFineStrategy;
import org.springframework.stereotype.Component;

@Component
public class utilFineCalculator {

    private FineStrategy strategy;

    public void setStrategy(FineStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculateFine(int daysLate) {
        if (strategy == null) {
            throw new IllegalStateException("Chưa thiết lập chiến lược tính phạt!");
        }
        return strategy.calculateFine(daysLate);
    }

    // Phương thức tiện lợi - tự động chọn strategy theo loại tài liệu
    public double calculateFineForDocument(Document document, int daysLate) {
        if (document instanceof com.library.entity.Book) {
            setStrategy(new BookFineStrategy());
        } else if (document instanceof com.library.entity.Magazine) {
            setStrategy(new MagazineFineStrategy());
        } else if (document instanceof com.library.entity.Journal) {
            setStrategy(new JournalFineStrategy());
        } else {
            setStrategy(new BookFineStrategy()); // default
        }
        return calculateFine(daysLate);
    }
}