package com.rac.ktm.midtown.controller;

import com.rac.ktm.midtown.dto.UserDto;
import com.rac.ktm.midtown.dto.requestDto.LoginRequestDto;
import com.rac.ktm.midtown.dto.requestDto.ProfileRequestDto;
import com.rac.ktm.midtown.dto.responseDto.LoginResponseDto;
import com.rac.ktm.midtown.dto.responseDto.ProfileResponseDto;
import com.rac.ktm.midtown.entity.News;
import com.rac.ktm.midtown.entity.Podcast;
import com.rac.ktm.midtown.entity.Post;
import com.rac.ktm.midtown.service.NewsService;
import com.rac.ktm.midtown.service.PodcastService;
import com.rac.ktm.midtown.service.PostService;
import com.rac.ktm.midtown.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rac")
public class UserController {

    private final UserService userService;
    @Autowired
    private PostService postService;

    @Autowired
    private NewsService newsService;
    @Autowired
    private PodcastService podcastService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/homePage")
    public String showHomePage(Model model, HttpSession session) {
        model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn") != null);
        model.addAttribute("loginError", null);

        Page<Post> posts = postService.findLatest(8);  // Get the latest 8 posts
        model.addAttribute("posts", posts);

        Page<Podcast> podcasts = podcastService.findLatest(3);  // Get the latest 3 podcasts
        model.addAttribute("podcasts", podcasts);

        Page<News> news = newsService.findLatest(3);  // Get the latest 3 news
        model.addAttribute("news", news);
        return "homePage";
    }

    @GetMapping("/events")
    public String showEvents(Model model, HttpSession session) {
        model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn") != null);
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "events";
    }

    @GetMapping("/news")
    public String showNewsPage(Model model, HttpSession session) {
        model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn") != null);
        List<News> news = newsService.findAll();
        model.addAttribute("news", news);
        return "news";
    }

    @GetMapping("/podcast")
    public String showPodcastPage(Model model, HttpSession session) {
        model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn") != null);
        List<Podcast> podcasts = podcastService.findAll();
        model.addAttribute("podcasts", podcasts);
        return "podcast";
    }

    @GetMapping("/about")
    public String showAboutPage(Model model, HttpSession session) {
        model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn") != null);
        return "about";
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model, HttpSession session) {
        if (session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn")) {
            String role = (String) session.getAttribute("role");
            if ("admin".equals(role)) {
                List<Post> posts = postService.findAll();
                model.addAttribute("posts", posts);
                return "admin";
            }
        }
        return "redirect:/rac/homePage";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "registrationPage";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult, Model model) {
        try {
            if (bindingResult.hasErrors()) {
                // Handle binding errors
                return "registrationPage";
            }

            userService.registerUser(userDto);
            return "homePage";
        } catch (IllegalArgumentException e) {
            model.addAttribute("registrationError", e.getMessage());
            return "registrationPage";
        } catch (Exception e) {
            model.addAttribute("registrationError", "An unexpected error occurred. Please try again.");
            return "registrationPage";
        }
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("loginRequestDto") LoginRequestDto loginRequestDto, BindingResult bindingResult, Model model, HttpSession session) {

        try {
            if (bindingResult.hasErrors()) {
                // Handle binding errors
                model.addAttribute("loginError", "⚠ Error in form submission ⚠");
                return "homePage";
            }

            // Example: Authenticate the user (replace this with your authentication logic)
            LoginResponseDto isAuthenticated = userService.authenticateUser(loginRequestDto);

            if (isAuthenticated != null) {
                session.setAttribute("isLoggedIn", true);  // Set login flag in session
                session.setAttribute("user", isAuthenticated.getUserName());
                session.setAttribute("role", isAuthenticated.getRole());
                session.removeAttribute("passwordError");
                model.addAttribute("loginError", null);
                return "redirect:/rac/homePage";  // Redirect to prevent form re-submission issues
            } else {
                // Handle authentication failure
                model.addAttribute("loginError", "⚠ Invalid username or password ⚠");
                return "homePage";
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            model.addAttribute("loginError", "⚠ An unexpected error occurred ⚠");
            return "homePage";
        } finally {
            // Clear passwordError attribute if it was set before
            model.addAttribute("passwordError", null);
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Clear the session
        return "redirect:/rac/homePage";  // Redirect to the homepage
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, HttpSession session, @RequestParam("username") String username) {
        if (session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn")) {
            model.addAttribute("userDto", new UserDto());
            ProfileRequestDto profileRequestDto = new ProfileRequestDto();
            profileRequestDto.setUserName(username);
            ProfileResponseDto profileResponseDto = userService.profileRequest(profileRequestDto);
            model.addAttribute("profileResponseDto", profileResponseDto);
            session.removeAttribute("passwordError");
            return "profile";
        }
        return "redirect:/rac/homePage";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute("profileResponseDto") ProfileResponseDto profileResponseDto, @RequestParam("currentPassword") String currentPassword, HttpSession session, Model model) {
        try {
            userService.updateProfile(profileResponseDto, currentPassword);
            return "redirect:/rac/homePage"; // Redirect to the home page after successful update
        } catch (IllegalArgumentException e) {
            // Handle specific validation errors and pass appropriate error messages to the view
            if (e.getMessage().contains("Email already exists")) {
                model.addAttribute("emailError", e.getMessage());
            } else if (e.getMessage().contains("Phone number already exists")) {
                model.addAttribute("phoneNumberError", e.getMessage());
            } else if (e.getMessage().contains("Current password is incorrect")) {
                model.addAttribute("passwordError", e.getMessage());
            }
            return "profile"; // Return to the profile page with an error message
        } catch (Exception e) {
            model.addAttribute("errorMessage", "⚠ An unexpected error occurred. Please try again. ⚠");
            return "profile"; // Return to the profile page with an error message
        }

    }
}
