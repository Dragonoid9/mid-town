package com.rac.ktm.midtown.repository;

import com.rac.ktm.midtown.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {
    void deleteById(Long id);

    Optional<Post> findById(Long id);

    List<Post> findAll();

    List<Post> findAllByOrderByDateDesc();

    Optional<Post> findByTitle(String title);

    List<Post> findByDateAndLocation(LocalDate date, String location);

    List<Post> findByDate(LocalDate eventDate);

    @Query("Select p from posts p order by p.date desc ")
    Page <Post> findTopByOrderByCreatedDateDesc(Pageable pageable);


}