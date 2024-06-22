package com.rac.ktm.midtown.controller;

import com.rac.ktm.midtown.entity.Post;
import com.rac.ktm.midtown.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/rac/post")
public class PostController {

    @Autowired
    private PostService postService;
    private final String UPLOAD_DIR = "src/main/resources/static/images/posts/";
    @GetMapping("/listpost")
    public String adminPage(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "admin";
    }

    @GetMapping("/create")
    public String createEventForm(Model model) {
        model.addAttribute("post", new Post());
        return "manageEvent/eventForm";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute Post post, @RequestParam("imageFile") MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(path.getParent()); // Ensure the directory exists
                Files.write(path, file.getBytes()); // Write the file to the specified path
                post.setImageUrl("/images/posts/" + fileName); // Set the URL for the image

            } catch (IOException e) {
                e.printStackTrace();
                // Handle file upload error
            }
        }

        postService.save(post, username);
        return "redirect:/rac/post/listpost";
    }

    @GetMapping("/edit/{id}")
    public String editEventForm(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        return "manageEvent/eventForm";
    }

    @PostMapping("/edit")
    public String editEvent(@ModelAttribute Post post, @RequestParam("imageFile") MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(path.getParent()); // Ensure the directory exists
                Files.write(path, file.getBytes()); // Write the file to the specified path
                post.setImageUrl("/images/posts/" + fileName); // Set the URL for the image

            } catch (IOException e) {
                e.printStackTrace();
                // Handle file upload error
            }
        }

        postService.save(post, username);
        return "redirect:/rac/post/listpost";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        postService.deleteById(id);
        return "redirect:/rac/post/listpost";
    }
}
