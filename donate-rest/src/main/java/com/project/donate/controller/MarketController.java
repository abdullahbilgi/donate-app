package com.project.donate.controller;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/markets")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MARKET','ROLE_BENEFACTOR')")
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MarketResponse>> getMarketsByUserId(@PathVariable Long userId)
    {
        return ResponseEntity.ok(marketService.getMarketsByUserId(userId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<MarketResponse> getMarketById(@PathVariable Long id) {
        return ResponseEntity.ok(marketService.getMarketById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MARKET')")
    public ResponseEntity<MarketResponse> createMarket(@RequestBody MarketRequest request) {
        return ResponseEntity.ok(marketService.createMarket(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MARKET')")
    public ResponseEntity<MarketResponse> updateMarket(@PathVariable Long id, @RequestBody MarketRequest request) {
        return ResponseEntity.ok(marketService.updateMarket(id,request));
    }

    @PostMapping("/enable/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MARKET')")
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MARKET','ROLE_USER')")
    public ResponseEntity<Void> deleteMarket(@PathVariable Long id) {
        marketService.deleteMarket(id);
        return ResponseEntity.noContent().build();
    }
}
