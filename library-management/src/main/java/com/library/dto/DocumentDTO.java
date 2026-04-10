package com.library.dto;

import com.library.entity.Book;
import com.library.entity.Document;
import com.library.entity.Journal;
import com.library.entity.Magazine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {

    private Long id;
    private String title;
    private String author;
    private String category;
    private int publicationYear;
    private int availableCopies;
    private String documentType;   // BOOK, MAGAZINE, hoặc JOURNAL

    // Phương thức chuyển đổi từ Entity sang DTO
    public static DocumentDTO fromEntity(Document document) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setAuthor(document.getAuthor());
        dto.setCategory(document.getCategory());
        dto.setPublicationYear(document.getPublicationYear());
        dto.setAvailableCopies(document.getAvailableCopies());

        // Xác định loại tài liệu (đa hình)
        if (document instanceof Book) {
            dto.setDocumentType("BOOK");
        } else if (document instanceof Magazine) {
            dto.setDocumentType("MAGAZINE");
        } else if (document instanceof Journal) {
            dto.setDocumentType("JOURNAL");
        } else {
            dto.setDocumentType("UNKNOWN");
        }

        return dto;
    }
}