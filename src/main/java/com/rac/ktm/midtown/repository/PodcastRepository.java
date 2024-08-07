package com.rac.ktm.midtown.repository;

import com.rac.ktm.midtown.entity.Podcast;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PodcastRepository extends JpaRepository<Podcast, Long> {
    void deleteById(Long id);

    Optional<Podcast> findById(Long id);

    List<Podcast> findAll();

    List<Podcast> findAllByOrderByCreatedDateDesc();

    Optional<Podcast> findByTitle(String title);

    Optional<Podcast> findByLink(String link);

    @Query("Select p from podcast p order by p.createdDate desc ")
    Page<Podcast> findTopByOrderByCreatedDateDesc(Pageable pageable);
}
