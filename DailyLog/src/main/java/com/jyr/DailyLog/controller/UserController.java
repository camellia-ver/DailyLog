package com.jyr.DailyLog.controller;

import com.jyr.DailyLog.dto.UserSignupRequestDto;
import com.jyr.DailyLog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model){
        model.addAttribute("signupForm", new UserSignupRequestDto());
        return "signup";
    }

    @GetMapping("/find-password")
    public String findPassword(){
        return "find-password";
    }
}
