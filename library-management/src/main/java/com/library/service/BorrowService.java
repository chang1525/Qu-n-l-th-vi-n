package com.library.service;

import com.library.entity.*;
import com.library.exception.*;
import com.library.repository.*;
import com.library.strategy.FineCalculator; // Sử dụng class trung gian để quản lý Strategy
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BorrowService implements IBorrowService {

    private final MemberRepository memberRepo;
    private final DocumentRepository docRepo;
    private final TransactionRepository transRepo;
    private final FineCalculator fineCalculator; // Tiêm FineCalculator đã có @Component của bạn

    @Override
    @Transactional
    public void borrowDocument(Long memberId, Long documentId) {
        // 1. Kiểm tra sự tồn tại của Member và Document
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy độc giả với ID: " + memberId));
        Document doc = docRepo.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài liệu với ID: " + documentId));

        // 2. Kiểm tra điều kiện mượn (Số lượng và Giới hạn mượn của Member)
        if (doc.getAvailableCopies() <= 0) {
            throw new BorrowLimitExceededException("Tài liệu '" + doc.getTitle() + "' đã hết bản sao.");
        }

        if (member.getCurrentBorrowCount() >= member.getMaxBorrowLimit()) {
            throw new BorrowLimitExceededException("Độc giả đã đạt giới hạn mượn tối đa (" + member.getMaxBorrowLimit() + ").");
        }

        // 3. Tạo giao dịch mới
        Transaction trans = new Transaction();
        trans.setMember(member);
        trans.setDocument(doc);
        trans.setBorrowDate(LocalDate.now());
        trans.setDueDate(LocalDate.now().plusDays(14)); // Mặc định mượn 14 ngày
        trans.setFineAmount(0.0);

        // 4. Cập nhật trạng thái và lưu
        doc.setAvailableCopies(doc.getAvailableCopies() - 1);
        member.setCurrentBorrowCount(member.getCurrentBorrowCount() + 1);

        transRepo.save(trans);
        docRepo.save(doc);
        memberRepo.save(member);
    }

    @Override
    @Transactional
    public void returnDocument(Long transactionId) {
        // 1. Tìm giao dịch trả
        Transaction trans = transRepo.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Giao dịch không tồn tại!"));

        if (trans.getReturnDate() != null) {
            throw new IllegalStateException("Giao dịch này đã hoàn tất (sách đã trả).");
        }

        // 2. Thiết lập ngày trả và tính tiền phạt
        LocalDate returnDate = LocalDate.now();
        trans.setReturnDate(returnDate);

        long daysLate = ChronoUnit.DAYS.between(trans.getDueDate(), returnDate);

        if (daysLate > 0) {
            // SỬA LỖI CHÍNH: Thay vì gọi trans.getDocument().calculateFine() dễ lỗi Hibernate Proxy,
            // ta dùng FineCalculator (Strategy Pattern) bạn đã viết để đảm bảo tính toán đúng loại.
            double fine = fineCalculator.calculateFineForDocument(trans.getDocument(), (int) daysLate);
            trans.setFineAmount(fine);
        }

        // 3. Hoàn trả số lượng cho Document và Member
        Document doc = trans.getDocument();
        doc.setAvailableCopies(doc.getAvailableCopies() + 1);

        Member member = trans.getMember();
        member.setCurrentBorrowCount(member.getCurrentBorrowCount() - 1);

        // 4. Lưu lại toàn bộ thay đổi
        transRepo.save(trans);
        docRepo.save(doc);
        memberRepo.save(member);
    }
}