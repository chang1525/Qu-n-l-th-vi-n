package com.library.controller;

import com.library.entity.Transaction;
import com.library.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionRepository transactionRepository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllTransactions() {
        List<Map<String, Object>> transactions = transactionRepository.findAll().stream()
                .map(this::toMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transactions);
    }

    private Map<String, Object> toMap(Transaction t) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", t.getId());
        map.put("memberName", t.getMember() != null ? t.getMember().getFullName() : "N/A");
        map.put("memberId", t.getMember() != null ? t.getMember().getId() : null);
        map.put("documentTitle", t.getDocument() != null ? t.getDocument().getTitle() : "N/A");
        map.put("documentId", t.getDocument() != null ? t.getDocument().getId() : null);
        map.put("borrowDate", t.getBorrowDate() != null ? t.getBorrowDate().toString() : null);
        map.put("dueDate", t.getDueDate() != null ? t.getDueDate().toString() : null);
        map.put("returnDate", t.getReturnDate() != null ? t.getReturnDate().toString() : null);
        map.put("fineAmount", t.getFineAmount());
        map.put("status", t.getReturnDate() == null ? "ĐANG MƯỢN" : "ĐÃ TRẢ");
        return map;
    }
}
