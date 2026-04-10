package com.library.controller;

import com.library.dto.DocumentDTO;
import com.library.service.ISearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SearchController {

    private final ISearchService searchService;

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> searchDocuments(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer year) {

        List<DocumentDTO> results = searchService.searchDocuments(keyword, author, category, year);
        return ResponseEntity.ok(results);
    }
}