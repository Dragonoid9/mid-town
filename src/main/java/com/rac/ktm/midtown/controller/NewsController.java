package com.rac.ktm.midtown.controller;

import com.rac.ktm.midtown.entity.News;
import com.rac.ktm.midtown.service.NewsService;
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
@RequestMapping("/rac/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    private final String UPLOAD_DIR = "src/main/resources/static/images/news/";

    @GetMapping("/listnews")
    public String adminPage(Model model) {
        List<News> news = newsService.findAll();
        model.addAttribute("news", news);
        return "manageNews/newsadmin";
    }

    @GetMapping("/create")
    public String createNewsForm(Model model) {
        model.addAttribute("news", new News());
        return "manageNews/newsForm";
    }

    @PostMapping("/create")
    public String createNews(@ModelAttribute News news, @RequestParam("imageFile") MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(path.getParent()); // Ensure the directory exists
                Files.write(path, file.getBytes()); // Write the file to the specified path
                news.setImageUrl("/images/news/" + fileName); // Set the URL for the image
            } catch (IOException e) {
                e.printStackTrace();
                // Handle file upload error
            }
        }

        newsService.save(news, username);
        return "redirect:/rac/news/listnews";
    }

    @GetMapping("/edit/{id}")
    public String editNewsForm(@PathVariable Long id, Model model) {
        News news = newsService.findById(id);
        model.addAttribute("news", news);
        return "manageNews/newsForm";
    }

    @PostMapping("/edit")
    public String editNews(@ModelAttribute News news, @RequestParam("imageFile") MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(path.getParent()); // Ensure the directory exists
                Files.write(path, file.getBytes()); // Write the file to the specified path
                news.setImageUrl("/images/news/" + fileName); // Set the URL for the image
            } catch (IOException e) {
                e.printStackTrace();
                // Handle file upload error
            }
        }

        newsService.save(news, username);
        return "redirect:/rac/news/listnews";
    }

    @GetMapping("/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsService.deleteById(id);
        return "redirect:/rac/news/listnews";
    }
}
