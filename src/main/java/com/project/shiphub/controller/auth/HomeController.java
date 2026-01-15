package com.project.shiphub.controller.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HomeController {
    @GetMapping("/")
    Map<String, String> status(){
        return Map.of("status","ok");
    }
}
