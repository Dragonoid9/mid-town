package com.rac.ktm.midtown.repository;

import com.rac.ktm.midtown.entity.News;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    void deleteById(Long id);

    Optional<News> findById(Long id);

    List<News> findAllByOrderByCreatedDateDesc();

    List<News> findAll();

    Optional<News> findByTitle(String title);
}
