package com.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;   // null = chưa trả

    private double fineAmount = 0.0;
}