package com.project.donate.controller;

import com.project.donate.dto.RegionDTO;
import com.project.donate.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/regions")
public class RegionController {
    
    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllCities() {
        return ResponseEntity.ok(regionService.getAllCities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getCityById(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.getRegionById(id));
    }

    @PostMapping
    public ResponseEntity<RegionDTO> createCity(@RequestBody RegionDTO regionDTO) {
        return ResponseEntity.ok(regionService.createRegion(regionDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> updateCity(@PathVariable Long id, @RequestBody RegionDTO regionDTO) {
        return ResponseEntity.ok(regionService.updateRegion(id,regionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}
