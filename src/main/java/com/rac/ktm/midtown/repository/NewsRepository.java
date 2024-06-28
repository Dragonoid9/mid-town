package com.rac.ktm.midtown.repository;

import com.rac.ktm.midtown.entity.News;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    void deleteById(Long id);

    Optional<News> findById(Long id);

    List<News> findAllByOrderByCreatedDateDesc();

    List<News> findAll();

    Optional<News> findByTitle(String title);
    @Query("Select p from news p order by p.createdDate desc ")
    Page<News> findTopByOrderByCreatedDateDesc(Pageable pageable);
}
