package com.m2i.warhammermarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ThymeleafPagesController {


    @GetMapping("/")
    public String index() {
        return "index";
    }



}
