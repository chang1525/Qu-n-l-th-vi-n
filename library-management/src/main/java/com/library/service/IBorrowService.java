package com.library.service;
public interface IBorrowService {
    void borrowDocument(Long memberId, Long documentId);
    void returnDocument(Long transactionId);
}