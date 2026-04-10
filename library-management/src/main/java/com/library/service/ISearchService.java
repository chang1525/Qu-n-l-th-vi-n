package com.library.service;

import com.library.dto.DocumentDTO;
import java.util.List;

public interface ISearchService {
    List<DocumentDTO> searchDocuments(String keyword, String author, String category, Integer year);
}