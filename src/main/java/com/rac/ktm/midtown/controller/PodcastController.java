package com.rac.ktm.midtown.controller;

import com.rac.ktm.midtown.entity.Podcast;
import com.rac.ktm.midtown.service.PodcastService;
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
@RequestMapping("/rac/podcast")
public class PodcastController {

    @Autowired
    private PodcastService podcastService;

    private final String UPLOAD_DIR = "src/main/resources/static/images/podcast/";

    @GetMapping("/listpodcast")
    public String adminPage(Model model, HttpSession session) {
        if (isAdmin(session)) {
            List<Podcast> podcast = podcastService.findAll();
            model.addAttribute("podcast", podcast);
            return "managePodcast/podcastadmin";
        }
        return "redirect:/rac/homePage";
    }

    @GetMapping("/create")
    public String createPodcastForm(Model model, HttpSession session) {
        if (isAdmin(session)) {
            model.addAttribute("podcast", new Podcast());
            return "managePodcast/podcastForm";
        }
        return "redirect:/rac/homePage";
    }

    @PostMapping("/create")
    public String createPodcast(@ModelAttribute Podcast podcast,
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
                    podcast.setImageUrl("/images/podcast/" + fileName); // Set the URL for the image
                }
                if (podcast.getId() != null) {
                    Podcast existingPodcast = podcastService.findById(podcast.getId());
                    podcast.setImageUrl(existingPodcast.getImageUrl()); // Retain the existing image URL
                }

                podcastService.save(podcast, username);
                return "redirect:/rac/podcast/listpodcast";
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                model.addAttribute("podcast", podcast);
                return "managePodcast/podcastForm";
            } catch (IOException e) {
                model.addAttribute("errorMessage", "File upload error: " + e.getMessage());
                model.addAttribute("podcast", podcast);
                return "managePodcast/podcastForm";
            }
        }
        return "redirect:/rac/homePage";
    }

    @GetMapping("/edit/{id}")
    public String editPodcastForm(@PathVariable Long id, Model model, HttpSession session) {
        if (isAdmin(session)) {
            Podcast podcast = podcastService.findById(id);
            model.addAttribute("podcast", podcast);
            return "managePodcast/podcastForm";
        }
        return "redirect:/rac/homePage";
    }

    @PostMapping("/edit")
    public String editPodcast(@ModelAttribute Podcast podcast,
                              @RequestParam("imageFile") MultipartFile file,
                              Model model,
                              @RequestParam("username") String username,
                              HttpSession session) {
        if (isAdmin(session)) {
            Podcast existingPodcast = podcastService.findById(podcast.getId());
            try {
                if (!file.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path path = Paths.get(UPLOAD_DIR, fileName);
                    Files.createDirectories(path.getParent()); // Ensure the directory exists
                    Files.write(path, file.getBytes()); // Write the file to the specified path
                    podcast.setImageUrl("/images/podcast/" + fileName); // Set the URL for the image
                } else {
                    podcast.setImageUrl(existingPodcast.getImageUrl()); // Retain the existing image URL
                }

                podcastService.save(podcast, username);
                return "redirect:/rac/podcast/listpodcast";
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                model.addAttribute("podcast", podcast);
                return "managePodcast/podcastForm";
            } catch (IOException e) {
                model.addAttribute("errorMessage", "File upload error: " + e.getMessage());
                model.addAttribute("podcast", podcast);
                return "managePodcast/podcastForm";
            }
        }
        return "redirect:/rac/homePage";
    }

    @GetMapping("/delete/{id}")
    public String deletePodcast(@PathVariable Long id, HttpSession session) {
        if (isAdmin(session)) {
            podcastService.deleteById(id);
            return "redirect:/rac/podcast/listpodcast";
        }
        return "redirect:/rac/homePage";
    }

    private boolean isAdmin(HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String role = (String) session.getAttribute("role");
        return isLoggedIn != null && isLoggedIn && "admin".equals(role);
    }
}
