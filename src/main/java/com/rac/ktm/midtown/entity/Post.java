package com.rac.ktm.midtown.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(name="posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;
    private LocalDate date;
    private LocalTime startTime;
    private double durationHours; // Duration in hours
    private String location;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String category;


    private String createdBy;
    private String updatedBy;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
}
