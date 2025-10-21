package com.health.controller;

import com.health.model.Family;
import com.health.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/families")
@CrossOrigin(origins = "http://localhost:4200")
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    @GetMapping
    public ResponseEntity<List<Family>> getAllFamilies() {
        List<Family> families = familyService.findAll();
        return ResponseEntity.ok(families);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Family> getFamilyById(@PathVariable Integer id) {
        return familyService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Family> createFamily(@Valid @RequestBody Family family) {
        Family savedFamily = familyService.save(family);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFamily);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Family> updateFamily(@PathVariable Integer id, @Valid @RequestBody Family familyDetails) {
        try {
            Family updatedFamily = familyService.update(id, familyDetails);
            return ResponseEntity.ok(updatedFamily);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFamily(@PathVariable Integer id) {
        try {
            familyService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Family>> getActiveFamilies() {
        List<Family> activeFamilies = familyService.findActiveFamilies();
        return ResponseEntity.ok(activeFamilies);
    }
}
