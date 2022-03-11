package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DTO.UniverseDTO;
import com.m2i.warhammermarket.service.UniverseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RequestMapping("/api")
public class UniverseController {

    @Autowired
    private UniverseService universeService;

    @GetMapping("/public/universes")
    public ResponseEntity<List<UniverseDTO>> getAllUniverses(String field, String type) {
        try {
            System.out.println("Exception dans getAllUniverses");
            List<UniverseDTO> list = this.universeService.findAll();
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            System.out.println("Exception dans getAllUniverses");
            return ResponseEntity.ok().body(null);
        }
    }

    @RequestMapping(value = "/public/universes", method = RequestMethod.POST) //passer en admin
    public ResponseEntity<UniverseDTO> createLabelUniverse(@RequestBody UniverseDTO universeDTO) throws Exception {
            System.out.println(universeDTO);
            UniverseDTO universe = universeService.saveUniverseDTO(universeDTO);
            return ResponseEntity.ok().body(universe);
    }
}
