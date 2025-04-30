package com.project.donate.controller;

import com.project.donate.dto.RegionDTO;
import com.project.donate.dto.Request.RegionRequest;
import com.project.donate.dto.Response.RegionResponse;
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
    public ResponseEntity<List<RegionResponse>> getAllCities() {
        return ResponseEntity.ok(regionService.getAllCities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionResponse> getCityById(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.getRegionById(id));
    }

    @PostMapping
    public ResponseEntity<RegionResponse> createCity(@RequestBody RegionRequest request) {
        return ResponseEntity.ok(regionService.createRegion(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionResponse> updateCity(@PathVariable Long id, @RequestBody RegionRequest request) {
        return ResponseEntity.ok(regionService.updateRegion(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}
