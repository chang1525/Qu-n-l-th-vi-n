package com.library.util;

import com.library.entity.Book;
import com.library.entity.Document;
import com.library.entity.Journal;
import com.library.entity.Magazine;

public class DocumentFactory {

    public static Document createDocument(String type, String title, String author,
                                          String category, int publicationYear, int copies) {

        Document document = switch (type.toUpperCase()) {
            case "BOOK" -> new Book();
            case "MAGAZINE" -> new Magazine();
            case "JOURNAL" -> new Journal();
            default -> throw new IllegalArgumentException("Loại tài liệu không hợp lệ: " + type);
        };

        document.setTitle(title);
        document.setAuthor(author);
        document.setCategory(category);
        document.setPublicationYear(publicationYear);
        document.setAvailableCopies(copies);

        return document;
    }
}