package com.rac.ktm.midtown.controller;

import com.rac.ktm.midtown.entity.News;
import com.rac.ktm.midtown.entity.Post;
import com.rac.ktm.midtown.service.PostService;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/detail/{id}")
    public String showPostDetail(@PathVariable("id") Long id, Model model) {
        // Retrieve post from service
        Post post = postService.findById(id);
        // Add post object to model
        model.addAttribute("post", post);
        return "detail"; // Assuming "postDetail.html" is your view template
    }

    @GetMapping("/listpost")
    public String adminPage(Model model,HttpSession session) {
        if (isAdmin(session)) {
            List<Post> posts = postService.findAll();
            model.addAttribute("posts", posts);
            return "admin";
        }
        return "redirect:/rac/homePage";
    }

    @GetMapping("/create")
    public String createEventForm(Model model,HttpSession session) {
        if (isAdmin(session)) {
            model.addAttribute("post", new Post());
            return "manageEvent/eventForm";
        }
        return "redirect:/rac/homePage";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute Post post,
                              @RequestParam("imageFile") MultipartFile file,
                              Model model,@RequestParam("username")String username,HttpSession session) {

        if (isAdmin(session)) {
            try {
                if (!file.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path path = Paths.get(UPLOAD_DIR, fileName);
                    Files.createDirectories(path.getParent()); // Ensure the directory exists
                    Files.write(path, file.getBytes()); // Write the file to the specified path
                    post.setImageUrl("/images/posts/" + fileName); // Set the URL for the image
                }
                if (post.getId() != null) {
                    Post existingPost = postService.findById(post.getId());
                    post.setImageUrl(existingPost.getImageUrl()); // Retain the existing image URL
                }
                postService.save(post, username);
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload file: " + e.getMessage());
                model.addAttribute("post", post); // Add post object to retain user input
                return "manageEvent/eventForm";
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
                model.addAttribute("post", post); // Add post object to retain user input
                return "manageEvent/eventForm";
            }

            return "redirect:/rac/post/listpost";
        }
        return "redirect:/rac/homePage";
    }

    @GetMapping("/edit/{id}")
    public String editEventForm(@PathVariable Long id, Model model,HttpSession session) {
        if (isAdmin(session)) {
            Post post = postService.findById(id);
            model.addAttribute("post", post);
            return "manageEvent/eventForm";
        }
        return "redirect:/rac/homePage";
    }

    @PostMapping("/edit")
    public String editEvent(@ModelAttribute Post post,
                            @RequestParam("imageFile") MultipartFile file,
                            @RequestParam("username") String username,
                            Model model,HttpSession session) {
        if (isAdmin(session)) {
            try {
                handleFileUpload(post, file);
                postService.save(post, username);
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload file: " + e.getMessage());
                model.addAttribute("post", post); // Add post object to retain user input
                return "manageEvent/eventForm";
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
                model.addAttribute("post", post); // Add post object to retain user input
                return "manageEvent/eventForm";
            }
            return "redirect:/rac/post/listpost";
        }
        return "redirect:/rac/homePage";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id, HttpSession session) {
        if (isAdmin(session)) {
            postService.deleteById(id);
            return "redirect:/rac/post/listpost";
        }
        return "redirect:/rac/homePage";
    }

    private void handleFileUpload(Post post, MultipartFile file) throws IOException {
        String UPLOAD_DIR = "src/main/resources/static/images/posts/";
        if (!file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR, fileName);
            Files.createDirectories(path.getParent()); // Ensure the directory exists
            Files.write(path, file.getBytes()); // Write the file to the specified path
            post.setImageUrl("/images/posts/" + fileName); // Set the URL for the image
        }
    }
    private boolean isAdmin(HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String role = (String) session.getAttribute("role");
        return isLoggedIn != null && isLoggedIn && "admin".equals(role);
    }
}
