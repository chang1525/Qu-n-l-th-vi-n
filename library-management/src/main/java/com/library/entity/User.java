package com.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Getter @Setter
public abstract class User extends BaseEntity {
    private String username;
    private String fullName;
    private String email;
    public abstract double calculateFine(int daysLate);
}