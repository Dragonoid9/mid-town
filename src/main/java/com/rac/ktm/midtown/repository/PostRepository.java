package com.rac.ktm.midtown.repository;

import com.rac.ktm.midtown.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {
    void deleteById(Long id);

    Optional<Post> findById(Long id);

    List<Post> findAll();

    List<Post> findAllByOrderByCreatedDateDesc();

    Optional<Post> findByTitle(String title);

    List<Post> findByDateAndLocation(LocalDate date, String location);

    List<Post> findByDate(LocalDate eventDate);
}