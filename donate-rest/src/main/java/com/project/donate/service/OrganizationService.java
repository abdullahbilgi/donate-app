package com.project.donate.service;

import com.project.donate.dto.Request.ApplyOrganizationRequest;
import com.project.donate.dto.Request.OrganizationRequest;
import com.project.donate.dto.Response.OrganizationResponse;
import com.project.donate.enums.Status;
import com.project.donate.model.Organization;
import com.project.donate.records.ProductItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrganizationService {

   // OrganizationResponse createOrganization(OrganizationRequest request);

    OrganizationResponse updateOrganization(Long id, OrganizationRequest request);

    List<OrganizationResponse> getAllOrganization();

    OrganizationResponse getOrganizationById(Long id);

    Organization getOrganizationEntityById(Long id);

    void enabledOrganization(Long id);

   // OrganizationResponse assignProduct(Long organizationId, ProductItem productItems);

    //OrganizationResponse removeProduct(Long organizationId, Long productId, int quantityToRemove);

    void deleteOrganization(Long id);

    Page<OrganizationResponse> getOrganizationsByStatusPageable(Status status , Pageable pageable);

    List<OrganizationResponse> getOrganizationsByStatus(Status status);

    OrganizationResponse applyOrganization(ApplyOrganizationRequest request);


    OrganizationResponse confirmOrganization(Long organizationId);

    OrganizationResponse rejectOrganization(Long organizationId);

    List<OrganizationResponse> getPendingOrganizations();
}
