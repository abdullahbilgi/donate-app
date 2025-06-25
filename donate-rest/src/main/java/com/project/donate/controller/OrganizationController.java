package com.project.donate.controller;

import com.project.donate.dto.Request.OrganizationRequest;
import com.project.donate.dto.Response.OrganizationResponse;
import com.project.donate.enums.Status;
import com.project.donate.records.ProductItem;
import com.project.donate.service.OrganizationService;
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
@RequestMapping("/api/v1/organizations")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MARKET','ROLE_BENEFACTOR')")
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganization());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<OrganizationResponse>> getMarketsByStatus(
            @PathVariable Status status,
            @PageableDefault(size = 12, sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(
                organizationService.getOrganizationsByStatusPageable(status, pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.getOrganizationById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_BENEFACTOR')")
    public ResponseEntity<OrganizationResponse> createOrganization(@RequestBody OrganizationRequest request) {
        return ResponseEntity.ok(organizationService.createOrganization(request));
    }

    @PostMapping("/{id}/assignProduct")
    public ResponseEntity<OrganizationResponse> assignProduct(@PathVariable Long id, @RequestBody ProductItem request) {
        return ResponseEntity.ok(organizationService.assignProduct(id, request));
    }

    @PostMapping("/{id}/removeProduct")
    public ResponseEntity<OrganizationResponse> removeProduct(
            @PathVariable Long id,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        return ResponseEntity.ok(organizationService.removeProduct(id, productId, quantity));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_BENEFACTOR')")
    public ResponseEntity<OrganizationResponse> updateOrganization(@PathVariable Long id, @RequestBody OrganizationRequest request) {
        return ResponseEntity.ok(organizationService.updateOrganization(id, request));
    }

    @PostMapping("/enable/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> enabledOrganization(@PathVariable Long id) {
        organizationService.enabledOrganization(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }
}
