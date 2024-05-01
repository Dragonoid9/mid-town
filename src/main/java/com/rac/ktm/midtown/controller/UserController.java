package com.rac.ktm.midtown.controller;

import com.rac.ktm.midtown.dto.UserDto;
import com.rac.ktm.midtown.dto.requestDto.LoginRequestDto;
import com.rac.ktm.midtown.dto.responseDto.LoginResponseDto;
import com.rac.ktm.midtown.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rac")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/homePage")
    public String showHomePage() {
        return "homePage";
    }

    @GetMapping("/events")
    public String showEvents(Model model) {
        return "events";
    }
    @GetMapping("/news")
    public String showNewsPage() {
        return "news";
    }

    @GetMapping("/podcast")
    public String showPodcastPage() {
        return "podcast";
    }

    @GetMapping("/index")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
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
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Pass error message to the error page
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            return "errorPage";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("loginRequestDto") LoginRequestDto loginRequestDto, BindingResult bindingResult,Model model) {
        // Implement your login logic here
        // Check username and password, authenticate the user, etc.
        // You might want to use Spring Security for a more comprehensive solution

        try {
            if (bindingResult.hasErrors()) {
                // Handle binding errors
                return "login";
            }

            // Example: Authenticate the user (replace this with your authentication logic)
            LoginResponseDto isAuthenticated = userService.authenticateUser(loginRequestDto);

            if (isAuthenticated !=null) {
                // Redirect to a success page or dashboard
                return "welcome";
            } else {
                // Redirect back to the login page with an error message
                model.addAttribute("loginError", "Invalid username or password");
                return "login";
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Redirect to the error page with an error message
            return "errorPage";
        }
    }

}
