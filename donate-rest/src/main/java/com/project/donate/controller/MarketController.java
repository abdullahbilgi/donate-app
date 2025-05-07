package com.project.donate.controller;

import com.project.donate.dto.MarketDTO;
import com.project.donate.dto.ProductDTO;
import com.project.donate.dto.Request.MarketRequest;
import com.project.donate.dto.Response.MarketResponse;
import com.project.donate.enums.Status;
import com.project.donate.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/markets")
public class MarketController {
    
    private final MarketService marketService;

    @GetMapping
    public ResponseEntity<List<MarketResponse>> getAllMarkets() {
        return ResponseEntity.ok(marketService.getAllMarket());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<MarketResponse>> getMarketsByStatus(
            @PathVariable Status status,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(
                marketService.getMarketsByStatusPageable(status,pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketResponse> getMarketById(@PathVariable Long id) {
        return ResponseEntity.ok(marketService.getMarketById(id));
    }

    @PostMapping
    public ResponseEntity<MarketResponse> createMarket(@RequestBody MarketRequest request) {
        return ResponseEntity.ok(marketService.createMarket(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketResponse> updateMarket(@PathVariable Long id, @RequestBody MarketRequest request) {
        return ResponseEntity.ok(marketService.updateMarket(id,request));
    }

    @PostMapping("/enable/{id}")
    public ResponseEntity<Void> enabledMarket(@PathVariable Long id) {
        marketService.enabledMarket(id);
        return ResponseEntity.ok().build();
    }

    /**@PostMapping("/{marketId}/product/{productId}")
    public ResponseEntity<Void> assignRole(@PathVariable("marketId") Long marketId,@PathVariable("productId") Long productId){
        marketService.assignProduct(marketId,productId);
        return ResponseEntity.ok().build();
    }
     **/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarket(@PathVariable Long id) {
        marketService.deleteMarket(id);
        return ResponseEntity.noContent().build();
    }
}
