package com.example.lab4Demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CityController {
    @Autowired
    private CityService cityService;

    @RequestMapping("/")
    public String search(@RequestParam(required = false) String name, Model model){
        model.addAttribute("name", name);
        model.addAttribute("cities", cityService.searchCitiesByName(name));
        return "index";
    }
}
