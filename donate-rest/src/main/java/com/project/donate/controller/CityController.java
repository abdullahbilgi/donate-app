package com.project.donate.controller;

import com.project.donate.dto.CityDTO;
import com.project.donate.dto.Request.CityRequest;
import com.project.donate.dto.Response.CityResponse;
import com.project.donate.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cities")
public class CityController {

    private final CityService cityService;


    @GetMapping
    public ResponseEntity<List<CityResponse>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getCityById(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    @PostMapping
    public ResponseEntity<CityResponse> createCity(@RequestBody CityRequest request) {
        return ResponseEntity.ok(cityService.createCity(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> updateCity(@PathVariable Long id, @RequestBody CityRequest request) {
        return ResponseEntity.ok(cityService.updateCity(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}
