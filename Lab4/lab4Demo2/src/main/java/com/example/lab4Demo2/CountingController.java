package com.example.lab4Demo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CountingController {
    @Autowired
    private Counter applicationCounter;
    @Autowired
    private Counter requestCounter;
    @Autowired
    private Counter sessionCounter;

    @GetMapping("/")
    public String showCounters(Model model){
        model.addAttribute("applicationCounter", applicationCounter.count());
        model.addAttribute("sessionCounter", sessionCounter.count());
        model.addAttribute("requestCounter", requestCounter.count());
        return "index";
    }
}
