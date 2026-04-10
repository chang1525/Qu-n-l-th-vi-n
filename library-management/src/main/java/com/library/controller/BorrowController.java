package com.library.controller;

import com.library.service.IBorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BorrowController {

    private final IBorrowService borrowService;

    @PostMapping("/{memberId}/{documentId}")
    public ResponseEntity<Map<String, Object>> borrowDocument(@PathVariable Long memberId, @PathVariable Long documentId) {
        borrowService.borrowDocument(memberId, documentId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "Mượn tài liệu thành công!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/return/{transactionId}")
    public ResponseEntity<Map<String, Object>> returnDocument(@PathVariable Long transactionId) {
        borrowService.returnDocument(transactionId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "Trả tài liệu thành công!");
        return ResponseEntity.ok(response);
    }
}