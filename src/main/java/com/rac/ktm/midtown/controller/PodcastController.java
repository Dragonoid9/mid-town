package com.rac.ktm.midtown.controller;

import com.rac.ktm.midtown.entity.Podcast;
import com.rac.ktm.midtown.service.PodcastService;
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
@RequestMapping("/rac/podcast")
public class PodcastController {

    @Autowired
    private PodcastService podcastService;
    private final String UPLOAD_DIR = "src/main/resources/static/images/podcast/";

    @GetMapping("/listpodcast")
    public String adminPage(Model model) {
        List<Podcast> podcast = podcastService.findAll();
        model.addAttribute("podcast", podcast);
        return "managePodcast/podcastadmin";
    }

    @GetMapping("/create")
    public String createPodcastForm(Model model) {
        model.addAttribute("podcast", new Podcast());
        return "managePodcast/podcastForm";
    }

    @PostMapping("/create")
    public String createPodcast(@ModelAttribute Podcast podcast, @RequestParam("imageFile") MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(path.getParent()); // Ensure the directory exists
                Files.write(path, file.getBytes()); // Write the file to the specified path
                podcast.setImageUrl("/images/podcast/" + fileName); // Set the URL for the image
            } catch (IOException e) {
                e.printStackTrace();
                // Handle file upload error
            }
        }

        podcastService.save(podcast, username);
        return "redirect:/rac/podcast/listpodcast";
    }

    @GetMapping("/edit/{id}")
    public String editPodcastForm(@PathVariable Long id, Model model) {
        Podcast podcast = podcastService.findById(id);
        model.addAttribute("podcast", podcast);
        return "managePodcast/podcastForm";
    }

    @PostMapping("/edit")
    public String editPodcast(@ModelAttribute Podcast podcast, @RequestParam("imageFile") MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(path.getParent()); // Ensure the directory exists
                Files.write(path, file.getBytes()); // Write the file to the specified path
                podcast.setImageUrl("/images/podcast/" + fileName); // Set the URL for the image
            } catch (IOException e) {
                e.printStackTrace();
                // Handle file upload error
            }
        }

        podcastService.save(podcast, username);
        return "redirect:/rac/podcast/listpodcast";
    }

    @GetMapping("/delete/{id}")
    public String deletePodcast(@PathVariable Long id) {
        podcastService.deleteById(id);
        return "redirect:/rac/podcast/listpodcast";
    }
}
