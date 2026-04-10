package com.library.repository;

import com.library.entity.Member;
import com.library.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Method dùng trong BorrowService
    boolean existsByMemberAndReturnDateIsNullAndDueDateBefore(Member member, LocalDate dueDate);

    // Tìm tất cả giao dịch đang mượn của một độc giả (nếu cần sau này)
    List<Transaction> findByMemberAndReturnDateIsNull(Member member);
}