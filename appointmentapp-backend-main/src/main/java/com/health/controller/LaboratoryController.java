package com.health.controller;

import com.health.model.Laboratory;
import com.health.service.LaboratoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/laboratories")
@CrossOrigin(origins = "http://localhost:4200")
public class LaboratoryController {

    @Autowired
    private LaboratoryService laboratoryService;

    @GetMapping
    public ResponseEntity<List<Laboratory>> getAllLaboratories() {
        List<Laboratory> laboratories = laboratoryService.findAll();
        return ResponseEntity.ok(laboratories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Laboratory> getLaboratoryById(@PathVariable Integer id) {
        return laboratoryService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Laboratory> createLaboratory(@Valid @RequestBody Laboratory laboratory) {
        Laboratory savedLaboratory = laboratoryService.save(laboratory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLaboratory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Laboratory> updateLaboratory(@PathVariable Integer id, @Valid @RequestBody Laboratory laboratoryDetails) {
        try {
            Laboratory updatedLaboratory = laboratoryService.update(id, laboratoryDetails);
            return ResponseEntity.ok(updatedLaboratory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLaboratory(@PathVariable Integer id) {
        try {
            laboratoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Laboratory>> getActiveLaboratories() {
        List<Laboratory> activeLaboratories = laboratoryService.findActiveLaboratories();
        return ResponseEntity.ok(activeLaboratories);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Laboratory>> searchLaboratoriesByName(@RequestParam String name) {
        List<Laboratory> laboratories = laboratoryService.findByNameContaining(name);
        return ResponseEntity.ok(laboratories);
    }
}
