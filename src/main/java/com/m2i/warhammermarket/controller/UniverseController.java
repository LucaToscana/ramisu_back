package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DTO.UniverseDTO;
import com.m2i.warhammermarket.service.UniverseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            List<UniverseDTO> list = this.universeService.findAll();
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            System.out.println("Exception dans getAllUniverses");
            return ResponseEntity.ok().body(null);
        }
    }

    /**
     * @author Brice Bayard
     * Add a new universe in table
     */
    @RequestMapping(value = "/commercial/addUniverse", method = RequestMethod.POST)
    public ResponseEntity<UniverseDTO> createLabelUniverse(@RequestBody UniverseDTO universeDTO) {
        try {
            UniverseDTO universe = universeService.saveUniverseDTO(universeDTO);
            return ResponseEntity.ok().body(universe);
        } catch (Exception e) {
            System.out.println("Exception dans createLabelUniverse");
            return ResponseEntity.ok().body(null);
        }
    }
}
