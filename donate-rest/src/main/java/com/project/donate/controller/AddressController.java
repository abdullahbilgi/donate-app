package com.project.donate.controller;

import com.project.donate.dto.Request.AddressRequest;
import com.project.donate.dto.Response.AddressResponse;
import com.project.donate.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/address")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MARKET','ROLE_BENEFACTOR')")
public class AddressController {
    
    private final AddressService addressService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AddressResponse>> getAllAddress() {
        return ResponseEntity.ok(addressService.getAllAddress());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }


    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.createAddress(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long id, @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(id,request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
