package com.rac.ktm.midtown.service;

import com.rac.ktm.midtown.entity.News;
import com.rac.ktm.midtown.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public List<News> findAll() {
        return newsRepository.findAllByOrderByCreatedDateDesc();
    }

    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found"));
    }

    public void save(News news, String currentUser) {
        if (news.getId() == null) {
            validateNewNews(news);
            news.setCreatedDate(LocalDateTime.now());
            news.setCreatedBy(currentUser);
            news.setUpdatedBy(null); // updatedBy is null on creation
        } else {
            validateUpdatedNews(news);
            News existingNews = newsRepository.findById(news.getId()).orElseThrow(() -> new RuntimeException("News not found"));
            news.setCreatedDate(existingNews.getCreatedDate()); // Retain original created date
            news.setCreatedBy(existingNews.getCreatedBy()); // Retain original created by
            news.setUpdatedDate(LocalDateTime.now());
            news.setUpdatedBy(currentUser);
        }
        newsRepository.save(news);
    }

    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }

    private void validateNewNews(News news) {
        if (newsRepository.findByTitle(news.getTitle()).isPresent()) {
            throw new IllegalArgumentException("News with the same title already exists.");
        }

        if (newsRepository.findAll().stream().anyMatch(n -> n.getLink().equals(news.getLink()))) {
            throw new IllegalArgumentException("News with the same link already exists.");
        }
    }

    private void validateUpdatedNews(News news) {
        newsRepository.findByTitle(news.getTitle()).ifPresent(existingNews -> {
            if (!existingNews.getId().equals(news.getId())) {
                throw new IllegalArgumentException("News with the same title already exists.");
            }
        });

        if (newsRepository.findAll().stream()
                .filter(n -> !n.getId().equals(news.getId()))
                .anyMatch(n -> n.getLink().equals(news.getLink()))) {
            throw new IllegalArgumentException("News with the same link already exists.");
        }
    }
}
