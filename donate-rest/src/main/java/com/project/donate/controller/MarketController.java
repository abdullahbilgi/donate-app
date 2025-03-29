package com.project.donate.controller;

import com.project.donate.dto.MarketDTO;
import com.project.donate.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/markets")
public class MarketController {
    
    private final MarketService marketService;

    @GetMapping
    public ResponseEntity<List<MarketDTO>> getAllMarkets() {
        return ResponseEntity.ok(marketService.getAllMarket());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketDTO> getCityById(@PathVariable Long id) {
        return ResponseEntity.ok(marketService.getMarketById(id));
    }

    @PostMapping
    public ResponseEntity<MarketDTO> createMarket(@RequestBody MarketDTO marketDTO) {
        return ResponseEntity.ok(marketService.createMarket(marketDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketDTO> updateMarket(@PathVariable Long id, @RequestBody MarketDTO marketDTO) {
        return ResponseEntity.ok(marketService.updateMarket(id,marketDTO));
    }

    @PostMapping("/enable/{id}")
    public ResponseEntity<Void> enabledMarket(@PathVariable Long id) {
        marketService.enabledMarket(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{marketId}/product/{productId}")
    public ResponseEntity<Void> assignRole(@PathVariable("marketId") Long marketId,@PathVariable("productId") Long productId){
        marketService.assignProduct(marketId,productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarket(@PathVariable Long id) {
        marketService.deleteMarket(id);
        return ResponseEntity.noContent().build();
    }
}
