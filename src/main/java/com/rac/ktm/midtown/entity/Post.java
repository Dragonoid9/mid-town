package com.rac.ktm.midtown.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // Initialize the list

    public Post(Long postId) {
        this.id = postId;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }
}
