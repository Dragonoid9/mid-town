package com.rac.ktm.midtown.controller;

import com.rac.ktm.midtown.dto.UserDto;
import com.rac.ktm.midtown.entity.User;
import com.rac.ktm.midtown.service.ManageUserService;
import com.rac.ktm.midtown.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rac/user")
public class ManageUserController {

    @Autowired
    ManageUserService manageUserService;

    @Autowired
    UserService userService;

    @GetMapping("/listusers")
    public String adminPage(Model model, HttpSession session) {
        if (isAdmin(session)) {
            List<User> users = manageUserService.findAll();
            model.addAttribute("users", users);
            return "manageUser/useradmin";
        }
        return "redirect:/rac/homePage";
    }

    @GetMapping("/create")
    public String showCreateUserForm(Model model, HttpSession session) {
        if (isAdmin(session)) {
            model.addAttribute("userDto", new UserDto());
            return "manageUser/createUser";
        }
        return "redirect:/rac/homePage";
    }

    @PostMapping("/create")
    public String registerUser(@ModelAttribute("userDto") UserDto userDto,
                               BindingResult bindingResult,
                               Model model,
                               HttpSession session) {
        if (isAdmin(session)) {
            try {
                if (bindingResult.hasErrors()) {
                    // Handle binding errors
                    return "manageUser/createUser";
                }

                userService.registerUser(userDto);
                return "redirect:/rac/user/listusers";
            } catch (IllegalArgumentException e) {
                model.addAttribute("registrationError", e.getMessage());
                return "manageUser/createUser";
            } catch (Exception e) {
                model.addAttribute("registrationError", "An unexpected error occurred. Please try again.");
                return "manageUser/createUser";
            }
        }
        return "redirect:/rac/homePage";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, HttpSession session) {
        if (isAdmin(session)) {
            manageUserService.deleteById(id);
            return "redirect:/rac/user/listusers";
        }
        return "redirect:/rac/homePage";
    }

    private boolean isAdmin(HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String role = (String) session.getAttribute("role");
        return isLoggedIn != null && isLoggedIn && "admin".equals(role);
    }
}
