package com.eventkita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to EventKita! 🎉");
        model.addAttribute("features", new String[]{
            "Find Amazing Events", 
            "Book Tickets Easily", 
            "Secure Payments"
        });
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}