package com.rac.ktm.midtown.controller;

import com.rac.ktm.midtown.entity.News;
import com.rac.ktm.midtown.service.NewsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String adminPage(Model model, HttpSession session) {
        if (isAdmin(session)) {
            List<News> news = newsService.findAll();
            model.addAttribute("news", news);
            return "manageNews/newsadmin";
        }
        return "redirect:/rac/homePage";
    }

    @GetMapping("/create")
    public String createNewsForm(Model model, HttpSession session) {
        if (isAdmin(session)) {
            model.addAttribute("news", new News());
            return "manageNews/newsForm";
        }
        return "redirect:/rac/homePage";
    }

    @PostMapping("/create")
    public String createNews(@ModelAttribute News news,
                             @RequestParam("imageFile") MultipartFile file,
                             Model model,
                             @RequestParam("username") String username,
                             HttpSession session) {
        if (isAdmin(session)) {
            try {
                if (!file.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path path = Paths.get(UPLOAD_DIR, fileName);
                    Files.createDirectories(path.getParent()); // Ensure the directory exists
                    Files.write(path, file.getBytes()); // Write the file to the specified path
                    news.setImageUrl("/images/news/" + fileName); // Set the URL for the image
                }
                if (news.getId() != null) {
                    News existingNews = newsService.findById(news.getId());
                    news.setImageUrl(existingNews.getImageUrl()); // Retain the existing image URL
                }

                newsService.save(news, username);
                return "redirect:/rac/news/listnews";
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                model.addAttribute("news", news);
                return "manageNews/newsForm";
            } catch (IOException e) {
                model.addAttribute("errorMessage", "File upload error: " + e.getMessage());
                model.addAttribute("news", news);
                return "manageNews/newsForm";
            }
        }
        return "redirect:/rac/homePage";
    }

    @GetMapping("/edit/{id}")
    public String editNewsForm(@PathVariable Long id, Model model, HttpSession session) {
        if (isAdmin(session)) {
            News news = newsService.findById(id);
            model.addAttribute("news", news);
            return "manageNews/newsForm";
        }
        return "redirect:/rac/homePage";
    }

    @PostMapping("/edit")
    public String editNews(@ModelAttribute News news,
                           @RequestParam("imageFile") MultipartFile file,
                           Model model,
                           @RequestParam("username") String username,
                           HttpSession session) {
        if (isAdmin(session)) {
            News existingNews = newsService.findById(news.getId());
            try {
                if (!file.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path path = Paths.get(UPLOAD_DIR, fileName);
                    Files.createDirectories(path.getParent()); // Ensure the directory exists
                    Files.write(path, file.getBytes()); // Write the file to the specified path
                    news.setImageUrl("/images/news/" + fileName); // Set the URL for the image
                } else {
                    news.setImageUrl(existingNews.getImageUrl()); // Retain the existing image URL
                }

                newsService.save(news, username);
                return "redirect:/rac/news/listnews";
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                model.addAttribute("news", news);
                return "manageNews/newsForm";
            } catch (IOException e) {
                model.addAttribute("errorMessage", "File upload error: " + e.getMessage());
                model.addAttribute("news", news);
                return "manageNews/newsForm";
            }
        }
        return "redirect:/rac/homePage";
    }

    @GetMapping("/delete/{id}")
    public String deleteNews(@PathVariable Long id, HttpSession session) {
        if (isAdmin(session)) {
            newsService.deleteById(id);
            return "redirect:/rac/news/listnews";
        }
        return "redirect:/rac/homePage";
    }

    private boolean isAdmin(HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String role = (String) session.getAttribute("role");
        return isLoggedIn != null && isLoggedIn && "admin".equals(role);
    }
}
