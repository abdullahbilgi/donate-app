package com.project.donate.service;

import com.project.donate.dto.OrganizationDTO;

import java.util.List;

public interface OrganizationService {

    OrganizationDTO createOrganization(OrganizationDTO organizationDTO);

    OrganizationDTO updateOrganization(Long id, OrganizationDTO organizationDTO);

    List<OrganizationDTO> getAllOrganization();

    OrganizationDTO getOrganizationById(Long id);
    
    void enabledOrganization(Long id);

    void deleteOrganization(Long id);


}
