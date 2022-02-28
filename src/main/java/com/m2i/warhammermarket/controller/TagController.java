package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DTO.TagDTO;
import com.m2i.warhammermarket.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RequestMapping("/api")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/public/tags")
    public ResponseEntity<List<TagDTO>> getAllTags(String field, String type) {
        List<TagDTO> list = this.tagService.findAll();
        return ResponseEntity.ok().body(list);
    }
}
