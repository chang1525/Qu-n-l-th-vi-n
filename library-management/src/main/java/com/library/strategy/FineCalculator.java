package com.library.strategy;

import com.library.entity.Document;
import org.springframework.stereotype.Component;

@Component
public class FineCalculator {

    private FineStrategy strategy;

    // Setter để thay đổi chiến lược tính phạt
    public void setStrategy(FineStrategy strategy) {
        this.strategy = strategy;
    }

    // Tính phạt theo chiến lược hiện tại
    public double calculateFine(int daysLate) {
        if (strategy == null) {
            throw new IllegalStateException("FineStrategy chưa được thiết lập");
        }
        return strategy.calculateFine(daysLate);
    }

    // Phương thức tiện ích: Tự động chọn Strategy dựa vào loại Document
    public double calculateFineForDocument(Document document, int daysLate) {
        if (document instanceof com.library.entity.Book) {
            setStrategy(new BookFineStrategy());
        } else if (document instanceof com.library.entity.Magazine) {
            setStrategy(new MagazineFineStrategy());
        } else if (document instanceof com.library.entity.Journal) {
            setStrategy(new JournalFineStrategy());
        } else {
            setStrategy(new BookFineStrategy()); // mặc định
        }

        return calculateFine(daysLate);
    }
}