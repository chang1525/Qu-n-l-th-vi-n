# 📚 Hệ Thống Quản Lý Thư Viện (Library Management System)

Ứng dụng quản lý thư viện full-stack được xây dựng với **Spring Boot** và giao diện **HTML/CSS/JavaScript** thuần.

---

## 📋 Mục Lục

- [Yêu Cầu Hệ Thống](#-yêu-cầu-hệ-thống)
- [Cài Đặt](#-cài-đặt)
- [Chạy Ứng Dụng](#-chạy-ứng-dụng)
- [Truy Cập Ứng Dụng](#-truy-cập-ứng-dụng)
- [Cấu Trúc Dự Án](#-cấu-trúc-dự-án)
- [Công Nghệ Sử Dụng](#-công-nghệ-sử-dụng)
- [Tính Năng](#-tính-năng)
- [API Endpoints](#-api-endpoints)

---

## 🔧 Yêu Cầu Hệ Thống

| Phần mềm       | Phiên bản tối thiểu |
|-----------------|----------------------|
| **Java JDK**    | 17 trở lên           |
| **Maven**       | 3.8+ (hoặc dùng Maven Wrapper có sẵn) |
| **MySQL**       | 8.0 trở lên          |

---

## 🛠 Cài Đặt

### 1. Cài đặt Java JDK 17+

- Tải từ [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) hoặc [Adoptium](https://adoptium.net/).
- Kiểm tra cài đặt:
  ```bash
  java -version
  ```

### 2. Cài đặt MySQL

- Tải từ [MySQL Community](https://dev.mysql.com/downloads/mysql/).
- Sau khi cài đặt, đảm bảo MySQL service đang chạy.

### 3. Cấu hình Database

Mở MySQL và tạo database (tùy chọn — ứng dụng sẽ tự tạo nếu chưa có):

```sql
CREATE DATABASE library_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. Cấu hình kết nối

Mở file `library-management/src/main/resources/application.properties` và chỉnh sửa thông tin kết nối MySQL cho phù hợp:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Ho_Chi_Minh
spring.datasource.username=root
spring.datasource.password=123456789   # ← Đổi thành mật khẩu MySQL của bạn
```

> ⚠️ **Lưu ý:** Thay `123456789` bằng mật khẩu MySQL thật của bạn.

---

## 🚀 Chạy Ứng Dụng

### Cách 1: Chạy bằng Maven (Command Line)

```bash
# Di chuyển vào thư mục project
cd library-management

# Chạy ứng dụng
mvnw.cmd spring-boot:run        # Windows
./mvnw spring-boot:run           # macOS / Linux
```

Nếu bạn đã cài Maven toàn cục:

```bash
cd library-management
mvn spring-boot:run
```

### Cách 2: Chạy bằng IDE (IntelliJ IDEA)

1. Mở IntelliJ IDEA → **File** → **Open** → Chọn thư mục `library-management`.
2. Đợi IntelliJ tải xong các dependency Maven.
3. Tìm file `LibraryApplication.java` tại:
   ```
   src/main/java/com/library/LibraryApplication.java
   ```
4. Click chuột phải → **Run 'LibraryApplication'**.

### Cách 3: Build JAR và chạy

```bash
cd library-management

# Build
mvnw.cmd clean package -DskipTests    # Windows
./mvnw clean package -DskipTests       # macOS / Linux

# Chạy file JAR
java -jar target/library-management-1.0.0.jar
```

---

## 🌐 Truy Cập Ứng Dụng

Sau khi ứng dụng khởi chạy thành công, truy cập:

| Trang              | URL                                          |
|--------------------|----------------------------------------------|
| **Trang chủ**      | http://localhost:8080                         |
| **Giao diện Web**  | http://localhost:8080/index.html              |
| **Swagger API UI** | http://localhost:8080/swagger-ui.html         |
| **API Docs (JSON)**| http://localhost:8080/v3/api-docs             |

> 💡 Dữ liệu mẫu (sách, thành viên) sẽ được tự động nạp khi khởi chạy lần đầu thông qua `DataInitializer.java`.

---

## 📁 Cấu Trúc Dự Án

```
library-management/
├── pom.xml                          # Cấu hình Maven & dependencies
├── src/
│   ├── main/
│   │   ├── java/com/library/
│   │   │   ├── LibraryApplication.java      # Entry point
│   │   │   ├── config/                      # Cấu hình (DataInitializer)
│   │   │   ├── controller/                  # REST API Controllers
│   │   │   │   ├── BorrowController.java
│   │   │   │   ├── MemberController.java
│   │   │   │   ├── SearchController.java
│   │   │   │   └── TransactionController.java
│   │   │   ├── dto/                         # Data Transfer Objects
│   │   │   ├── entity/                      # JPA Entities
│   │   │   │   ├── BaseEntity.java
│   │   │   │   ├── Document.java
│   │   │   │   ├── Book.java
│   │   │   │   ├── Journal.java
│   │   │   │   ├── Magazine.java
│   │   │   │   ├── Member.java
│   │   │   │   ├── Transaction.java
│   │   │   │   └── User.java
│   │   │   ├── exception/                   # Xử lý ngoại lệ
│   │   │   ├── repository/                  # JPA Repositories
│   │   │   ├── service/                     # Business logic
│   │   │   ├── strategy/                    # Design patterns
│   │   │   └── util/                        # Tiện ích
│   │   └── resources/
│   │       ├── application.properties       # Cấu hình ứng dụng
│   │       └── static/                      # Frontend
│   │           ├── index.html
│   │           ├── style.css
│   │           └── app.js
│   └── test/                                # Unit tests
```

---

## 🧰 Công Nghệ Sử Dụng

### Backend
- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Data JPA** — ORM & truy vấn database
- **Hibernate** — JPA Implementation
- **MySQL 8** — Cơ sở dữ liệu
- **Lombok** — Giảm boilerplate code
- **Spring Validation** — Kiểm tra dữ liệu đầu vào
- **SpringDoc OpenAPI 2.6** — Swagger UI tự động

### Frontend
- **HTML5 / CSS3 / JavaScript** (Vanilla)
- Giao diện responsive, hiện đại

---

## ✨ Tính Năng

- 📖 **Quản lý tài liệu**: Sách, Tạp chí, Báo khoa học (Book, Magazine, Journal)
- 👥 **Quản lý thành viên**: Thêm, xem, tìm kiếm thành viên
- 🔄 **Mượn / Trả sách**: Quy trình mượn-trả đầy đủ
- 📊 **Lịch sử giao dịch**: Theo dõi tất cả các giao dịch mượn-trả
- 🔍 **Tìm kiếm**: Tìm kiếm tài liệu theo nhiều tiêu chí
- 📄 **Swagger UI**: Giao diện test API trực quan
- 🌏 **Hỗ trợ UTF-8**: Hiển thị tiếng Việt đầy đủ

---

## 📡 API Endpoints

### Tài liệu (Documents)
| Method | Endpoint                  | Mô tả                    |
|--------|---------------------------|---------------------------|
| GET    | `/api/search`             | Tìm kiếm tài liệu       |
| GET    | `/api/search/all`         | Lấy tất cả tài liệu     |

### Thành viên (Members)
| Method | Endpoint                  | Mô tả                    |
|--------|---------------------------|---------------------------|
| GET    | `/api/members`            | Lấy danh sách thành viên |
| POST   | `/api/members`            | Thêm thành viên mới      |

### Mượn / Trả (Borrow)
| Method | Endpoint                  | Mô tả                    |
|--------|---------------------------|---------------------------|
| POST   | `/api/borrow`             | Mượn tài liệu            |
| POST   | `/api/borrow/return`      | Trả tài liệu             |

### Giao dịch (Transactions)
| Method | Endpoint                  | Mô tả                    |
|--------|---------------------------|---------------------------|
| GET    | `/api/transactions`       | Xem lịch sử giao dịch    |

> 📌 Truy cập **Swagger UI** tại `http://localhost:8080/swagger-ui.html` để xem chi tiết và test tất cả API.

---

## ❓ Xử Lý Sự Cố

| Vấn đề | Giải pháp |
|--------|-----------|
| `Access denied for user 'root'` | Kiểm tra lại username/password MySQL trong `application.properties` |
| `Port 8080 already in use` | Đổi port trong `application.properties`: `server.port=8081` |
| `Cannot connect to MySQL` | Đảm bảo MySQL service đang chạy: `net start mysql` (Windows) |
| Lỗi encoding tiếng Việt | Đảm bảo database dùng charset `utf8mb4` |

---

## 📄 License

Dự án này được phát triển cho mục đích học tập và nghiên cứu.