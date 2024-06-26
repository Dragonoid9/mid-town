package com.rac.ktm.midtown.service;

import com.rac.ktm.midtown.entity.Podcast;
import com.rac.ktm.midtown.repository.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PodcastService {

    @Autowired
    private PodcastRepository podcastRepository;

    public List<Podcast> findAll() {
        return podcastRepository.findAllByOrderByCreatedDateDesc();
    }

    public Podcast findById(Long id) {
        return podcastRepository.findById(id).orElseThrow(() -> new RuntimeException("Podcast not found"));
    }

    public void save(Podcast podcast, String currentUser) {
        if (podcast.getId() == null) {
            validateNewPodcast(podcast);
            podcast.setCreatedDate(LocalDateTime.now());
            podcast.setCreatedBy(currentUser);
            podcast.setUpdatedBy(null); // updatedBy is null on creation
        } else {
            validateUpdatedPodcast(podcast);
            Podcast existingPodcast = podcastRepository.findById(podcast.getId()).orElseThrow(() -> new RuntimeException("Podcast not found"));
            podcast.setCreatedDate(existingPodcast.getCreatedDate()); // Retain original created date
            podcast.setCreatedBy(existingPodcast.getCreatedBy()); // Retain original created by
            podcast.setUpdatedDate(LocalDateTime.now());
            podcast.setUpdatedBy(currentUser);
        }
        podcastRepository.save(podcast);
    }

    public void deleteById(Long id) {
        podcastRepository.deleteById(id);
    }

    private void validateNewPodcast(Podcast podcast) {
        if (podcastRepository.findByTitle(podcast.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Podcast with the same title already exists.");
        }

        if (podcastRepository.findByLink(podcast.getLink()).isPresent()) {
            throw new IllegalArgumentException("Podcast with the same link already exists.");
        }
    }

    private void validateUpdatedPodcast(Podcast podcast) {
        podcastRepository.findByTitle(podcast.getTitle()).ifPresent(existingPodcast -> {
            if (!existingPodcast.getId().equals(podcast.getId())) {
                throw new IllegalArgumentException("Podcast with the same title already exists.");
            }
        });

        podcastRepository.findByLink(podcast.getLink()).ifPresent(existingPodcast -> {
            if (!existingPodcast.getId().equals(podcast.getId())) {
                throw new IllegalArgumentException("Podcast with the same link already exists.");
            }
        });
    }
}
