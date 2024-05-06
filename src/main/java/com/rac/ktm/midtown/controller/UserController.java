package com.rac.ktm.midtown.controller;

import com.rac.ktm.midtown.dto.UserDto;
import com.rac.ktm.midtown.dto.requestDto.LoginRequestDto;
import com.rac.ktm.midtown.dto.responseDto.LoginResponseDto;
import com.rac.ktm.midtown.service.UserService;
import jakarta.servlet.http.HttpSession;
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
    public String showHomePage(Model model, HttpSession session) {
        model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn") != null);
        return "homePage";
    }

    @GetMapping("/events")
    public String showEvents(Model model, HttpSession session) {
        model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn") != null);
        return "events";
    }
    @GetMapping("/news")
    public String showNewsPage(Model model, HttpSession session) {
        model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn") != null);
        return "news";
    }

    @GetMapping("/podcast")
    public String showPodcastPage(Model model, HttpSession session) {
        model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn") != null);
        return "podcast";
    }

    @GetMapping("/about")
    public String showAboutPage(Model model, HttpSession session) {
        model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn") != null);
        return "about";
    }

    @GetMapping("/index")
    public String showIndexPage() {
        return "index";
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
    public String loginUser(@ModelAttribute("loginRequestDto") LoginRequestDto loginRequestDto, BindingResult bindingResult,Model model, HttpSession session) {

        try {
            if (bindingResult.hasErrors()) {
                // Handle binding errors
                    model.addAttribute("loginError", "Error in form submission");
                    return "homePage";
            }

            // Example: Authenticate the user (replace this with your authentication logic)
            LoginResponseDto isAuthenticated = userService.authenticateUser(loginRequestDto);

            if (isAuthenticated !=null) {
                session.setAttribute("isLoggedIn", true);  // Set login flag in session
                session.setAttribute("user", isAuthenticated.getUsername());
                return "redirect:/rac/homePage";  // Redirect to prevent form re-submission issues
            } else {
                // Redirect back to the login page with an error message
                model.addAttribute("loginError", "Invalid username or password");
                return "homePage";
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Redirect to the error page with an error message
            return "errorPage";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Clear the session
        return "redirect:/rac/homePage";  // Redirect to the homepage
    }

}
