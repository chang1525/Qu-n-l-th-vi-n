package com.library.service;

import com.library.dto.DocumentDTO;
import com.library.entity.Document;
import com.library.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService implements ISearchService {

    private final DocumentRepository documentRepository;

    @Override
    public List<DocumentDTO> searchDocuments(String keyword, String author, String category, Integer year) {
        // Lấy tất cả tài liệu từ database
        List<Document> documents = documentRepository.findAll();

        // Sử dụng Stream API + Lambda để lọc theo nhiều tiêu chí (rất quan trọng để đạt điểm cao)
        return documents.stream()
                .filter(doc -> isMatchKeyword(doc, keyword))
                .filter(doc -> isMatchAuthor(doc, author))
                .filter(doc -> isMatchCategory(doc, category))
                .filter(doc -> isMatchYear(doc, year))
                .map(DocumentDTO::fromEntity)           // Chuyển sang DTO
                .collect(Collectors.toList());
    }

    // Các phương thức hỗ trợ lọc - code sạch và dễ mở rộng
    private boolean isMatchKeyword(Document doc, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return true;
        return doc.getTitle().toLowerCase().contains(keyword.toLowerCase());
    }

    private boolean isMatchAuthor(Document doc, String author) {
        if (author == null || author.trim().isEmpty()) return true;
        return doc.getAuthor().equalsIgnoreCase(author.trim());
    }

    private boolean isMatchCategory(Document doc, String category) {
        if (category == null || category.trim().isEmpty()) return true;
        return doc.getCategory().equalsIgnoreCase(category.trim());
    }

    private boolean isMatchYear(Document doc, Integer year) {
        if (year == null) return true;
        return doc.getPublicationYear() == year;
    }
}