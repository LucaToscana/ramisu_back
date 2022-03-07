package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DTO.UniverseDTO;
import com.m2i.warhammermarket.service.UniverseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RequestMapping("/api")
public class UniverseController {

    @Autowired
    private UniverseService universeService;

    @GetMapping("/public/universes")
    public ResponseEntity<List<UniverseDTO>> getAllUniverses(String field, String type) {
        List<UniverseDTO> list = this.universeService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @RequestMapping(value = "/public/universes", method = RequestMethod.POST)
    public String postLabelUniverse() throws Exception {
        return "Universes posted !";
    }
}
