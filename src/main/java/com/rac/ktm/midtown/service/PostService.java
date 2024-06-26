package com.rac.ktm.midtown.service;

import com.rac.ktm.midtown.entity.Post;
import com.rac.ktm.midtown.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }
    public Page<Post> findLatest(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<Post> posts= postRepository.findTopByOrderByCreatedDateDesc(pageable);
        System.out.println("Fetched " + posts.getContent() + " posts");
        return posts;
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void save(Post post, String currentUser) {
        if(post.getId()==null) {
            validateNewPost(post);
            post.setCreatedDate(LocalDateTime.now());
            post.setCreatedBy(currentUser);
            post.setUpdatedBy(null); // updatedBy is null on creation
            postRepository.save(post);
        }
     else{
            validateUpdatedPost(post);
            Post existingPost = postRepository.findById(post.getId())
                    .orElseThrow(() -> new RuntimeException("Post not found"));
            post.setCreatedDate(existingPost.getCreatedDate()); // Retain original created date
            post.setCreatedBy(existingPost.getCreatedBy()); // Retain original created by
            post.setUpdatedDate(LocalDateTime.now());
            post.setUpdatedBy(currentUser);
            postRepository.save(post);
        }
    }


    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    private void validateNewPost(Post newPost) {
        // Check if there are already two events on the same day
        LocalDate eventDate = newPost.getDate();
        List<Post> eventsOnSameDay = postRepository.findByDate(eventDate);
        if (eventsOnSameDay.size() >= 2) {
            throw new IllegalArgumentException("Cannot create more than two events on the same day.");
        }

        // Check if there is another event at the same location
        if (postRepository.findByDateAndLocation(eventDate, newPost.getLocation()).stream()
                .anyMatch(existingPost -> !existingPost.getId().equals(newPost.getId()))) {
            throw new IllegalArgumentException("Another event exists at the same location.");
        }

        // Check if there is another event with the same title
        if (postRepository.findByTitle(newPost.getTitle()).isPresent()) {
            throw new IllegalArgumentException("An event with the same title already exists.");
        }

        // Check for time gap between events on the same day
        List<Post> eventsWithTimeConflicts = eventsOnSameDay.stream()
                .filter(existingPost -> isTimeConflict(existingPost, newPost))
                .collect(Collectors.toList());
        if (!eventsWithTimeConflicts.isEmpty()) {
            throw new IllegalArgumentException("There should be at least 2hr time gap between events on the same day.");
        }
    }

    private void validateUpdatedPost(Post updatedPost) {
        // Check if there are already two events on the same day
        LocalDate eventDate = updatedPost.getDate();
        List<Post> eventsOnSameDay = postRepository.findByDate(eventDate);
        long countEventsOnSameDay = eventsOnSameDay.stream()
                .filter(post -> !post.getId().equals(updatedPost.getId())) // Exclude current post
                .count();
        if (countEventsOnSameDay >= 2) {
            throw new IllegalArgumentException("Cannot have more than two events on the same day.");
        }

        // Check if there is another event at the same location
        boolean eventAtSameLocationExists = eventsOnSameDay.stream()
                .anyMatch(post -> !post.getId().equals(updatedPost.getId()) && post.getLocation().equals(updatedPost.getLocation()));
        if (eventAtSameLocationExists) {
            throw new IllegalArgumentException("Another event exists at the same location.");
        }

        // Check if there is another event with the same title
        postRepository.findByTitle(updatedPost.getTitle()).ifPresent(existingPost -> {
            // Ensure it's not comparing the same entity instance
            if (!existingPost.getId().equals(updatedPost.getId())) {
                throw new IllegalArgumentException("An event with the same title already exists.");
            }
        });

        // Check for time gap between events on the same day
        List<Post> eventsWithTimeConflicts = eventsOnSameDay.stream()
                .filter(existingPost -> isTimeConflict(existingPost, updatedPost))
                .filter(existingPost -> !existingPost.getId().equals(updatedPost.getId()))
                .collect(Collectors.toList());
        if (!eventsWithTimeConflicts.isEmpty()) {
            throw new IllegalArgumentException("There should be at least 2hr time gap between events on the same day.");
        }
    }

    private boolean isTimeConflict(Post post1, Post post2) {
        // Calculate end times for both posts
        LocalDateTime endTime1 = post1.getDate().atTime(post1.getStartTime()).plusHours((long) post1.getDurationHours());
        LocalDateTime endTime2 = post2.getDate().atTime(post2.getStartTime()).plusHours((long) post2.getDurationHours());

        // Check for overlap or conflict
        return post1.getDate().equals(post2.getDate()) && // Check if events are on the same day
                post1.getStartTime().isBefore(endTime2.toLocalTime()) && // Post 1 starts before post 2 ends
                post2.getStartTime().isBefore(endTime1.toLocalTime());   // Post 2 starts before post 1 ends
    }

}
