package com.busanit501.testdbmaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/upload")
    public String uploadForm() {
        return "upload";
    }
}