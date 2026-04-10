package com.library.config;

import com.library.entity.Book;
import com.library.entity.Journal;
import com.library.entity.Magazine;
import com.library.entity.Member;
import com.library.repository.DocumentRepository;
import com.library.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(DocumentRepository docRepo, MemberRepository memberRepo) {
        return args -> {
            // Chỉ nạp dữ liệu nếu chưa có
            if (docRepo.count() > 0) {
                System.out.println("Dữ liệu đã tồn tại, bỏ qua khởi tạo.");
                return;
            }

            System.out.println("Đang nạp dữ liệu mẫu...");

            // Tạo độc giả
            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setFullName("Nguyễn Văn A");
            member1.setEmail("vana@gmail.com");
            member1.setMaxBorrowLimit(5);
            member1.setCurrentBorrowCount(0);
            memberRepo.save(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setFullName("Trần Thị B");
            member2.setEmail("thib@gmail.com");
            member2.setMaxBorrowLimit(3);
            member2.setCurrentBorrowCount(0);
            memberRepo.save(member2);

            // Tạo sách
            Book book1 = new Book();
            book1.setTitle("Java OOP Cơ Bản");
            book1.setAuthor("James Gosling");
            book1.setCategory("Công nghệ");
            book1.setPublicationYear(2023);
            book1.setAvailableCopies(10);
            book1.setIsbn("ISBN-001");
            docRepo.save(book1);

            Book book2 = new Book();
            book2.setTitle("Spring Boot in Action");
            book2.setAuthor("Craig Walls");
            book2.setCategory("Công nghệ");
            book2.setPublicationYear(2024);
            book2.setAvailableCopies(5);
            book2.setIsbn("ISBN-002");
            docRepo.save(book2);

            Book book3 = new Book();
            book3.setTitle("Python Programming");
            book3.setAuthor("Guido van Rossum");
            book3.setCategory("Công nghệ");
            book3.setPublicationYear(2023);
            book3.setAvailableCopies(7);
            book3.setIsbn("ISBN-003");
            docRepo.save(book3);

            // Tạo tạp chí
            Magazine mag1 = new Magazine();
            mag1.setTitle("Tạp chí Khoa học");
            mag1.setAuthor("Viện Khoa học VN");
            mag1.setCategory("Khoa học");
            mag1.setPublicationYear(2025);
            mag1.setAvailableCopies(15);
            mag1.setIssueNumber("VOL-2025-01");
            docRepo.save(mag1);

            Magazine mag2 = new Magazine();
            mag2.setTitle("Tạp chí Văn học");
            mag2.setAuthor("Hội Nhà văn VN");
            mag2.setCategory("Văn học");
            mag2.setPublicationYear(2025);
            mag2.setAvailableCopies(12);
            mag2.setIssueNumber("VOL-2025-02");
            docRepo.save(mag2);

            // Tạo tập san
            Journal journal1 = new Journal();
            journal1.setTitle("Tập san Y học");
            journal1.setAuthor("Bộ Y tế");
            journal1.setCategory("Y khoa");
            journal1.setPublicationYear(2024);
            journal1.setAvailableCopies(8);
            journal1.setVolume("Vol.12");
            docRepo.save(journal1);

            System.out.println("Nạp dữ liệu mẫu thành công! (" + docRepo.count() + " tài liệu, " + memberRepo.count() + " độc giả)");
        };
    }
}
