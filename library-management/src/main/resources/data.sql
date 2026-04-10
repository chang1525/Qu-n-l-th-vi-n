-- Thiết lập charset UTF-8
SET NAMES utf8mb4;
ALTER DATABASE library_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Nạp người dùng (Bảng users - cha)
INSERT INTO users (id, username, full_name, email)
VALUES (1, 'admin', N'Thủ thư Admin', 'admin@library.com'),
       (2, 'member1', N'Nguyễn Văn A', 'vana@gmail.com'),
       (3, 'member2', N'Trần Thị B', 'thib@gmail.com');

-- Nạp độc giả (Bảng member - con)
INSERT INTO member (id, max_borrow_limit, current_borrow_count)
VALUES (2, 5, 0),
       (3, 3, 0);

-- Nạp tài liệu (Sách, Tạp chí, Tập san)
INSERT INTO document (id, title, author, category, publication_year, available_copies, doc_type, isbn, issue_number, volume)
VALUES (1, N'Java OOP Cơ Bản', 'James Gosling', N'Công nghệ', 2023, 10, 'Book', 'ISBN-001', NULL, NULL),
       (2, N'Tạp chí Khoa học', N'Viện Khoa học VN', N'Khoa học', 2025, 15, 'Magazine', NULL, 'VOL-2025-01', NULL),
       (3, 'Spring Boot in Action', 'Craig Walls', N'Công nghệ', 2024, 5, 'Book', 'ISBN-002', NULL, NULL),
       (4, N'Tập san Y học', N'Bộ Y tế', N'Y khoa', 2024, 8, 'Journal', NULL, NULL, 'Vol.12'),
       (5, 'Python Programming', 'Guido van Rossum', N'Công nghệ', 2023, 7, 'Book', 'ISBN-003', NULL, NULL),
       (6, N'Tạp chí Văn học', N'Hội Nhà văn VN', N'Văn học', 2025, 12, 'Magazine', NULL, 'VOL-2025-02', NULL);