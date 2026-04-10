package com.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "doc_type")
@Getter
@Setter
public abstract class Document extends BaseEntity {

    private String title;
    private String author;
    private String category;
    private int publicationYear;
    private int availableCopies;

    // Phương thức trừu tượng - đa hình
    public abstract double calculateFine(int daysLate);
}