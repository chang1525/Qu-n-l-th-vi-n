# HỆ THỐNG QUẢN LÝ THƯ VIỆN - Java OOP + Spring Boot

## Giới thiệu
Dự án quản lý thư viện sử dụng Spring Boot 3, JPA, MySQL, thể hiện đầy đủ 4 tính chất OOP và các Design Pattern.

## Công nghệ sử dụng
- Java 21 + Spring Boot 3.3.4
- Spring Data JPA + Hibernate
- MySQL
- Lombok, Validation, Swagger UI
- Design Patterns: Singleton, Factory, Strategy

## Cấu trúc project (Layered Architecture)
- `controller` → Nhận request
- `service` → Xử lý logic nghiệp vụ
- `repository` → Truy vấn database
- `entity` → Các lớp thực thể
- `dto` → Data Transfer Object
- `util` & `strategy` → Design Patterns

## Cách cài đặt & chạy

1. Tạo database MySQL tên `library_db`
2. Cập nhật username/password trong `application.properties`
3. Mở project bằng IntelliJ IDEA
4. Reload Maven
5. Chạy `LibraryApplication.java`

## API chính (Swagger UI)
Truy cập: http://localhost:8080/swagger-ui.html

- **Mượn sách**: `POST /api/borrow/{memberId}/{documentId}`
- **Tìm kiếm**: `GET /api/search?keyword=...&author=...`

## Minh chứng OOP & Design Patterns

**1. Tính đóng gói (Encapsulation)**:  
Tất cả thuộc tính trong Entity đều `private`, truy cập qua `@Getter @Setter` của Lombok.

**2. Tính kế thừa (Inheritance)**:
- `User` → `Member`, `Librarian`
- `Document` → `Book`, `Magazine`, `Journal`
- Tất cả Entity kế thừa `BaseEntity`

**3. Tính trừu tượng (Abstraction)**:
- Abstract class: `Document`, `User`, `BaseEntity`
- Interface: `IBorrowService`, `ISearchService`

**4. Tính đa hình (Polymorphism)**:  
Phương thức `calculateFine()` được override khác nhau ở mỗi loại `Document`.

**Design Patterns đã áp dụng**:
- **Singleton**: `DatabaseConnection.getInstance()`
- **Factory**: `DocumentFactory.createDocument(...)`
- **Strategy**: `FineCalculator` + các `*FineStrategy`

## Unit Test & Transaction
- Sử dụng `@Transactional` đảm bảo tính toàn vẹn dữ liệu khi mượn sách.
- Tìm kiếm sử dụng Java Stream API + Lambda.