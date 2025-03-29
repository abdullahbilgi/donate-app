package com.project.donate.mapper;


import com.project.donate.dto.OrganizationDTO;
import com.project.donate.model.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper implements ObjectMapper<Organization, OrganizationDTO> {
    @Override
    public OrganizationDTO map(Organization organization) {
        return OrganizationDTO.builder()
                .id(organization.getId())
                .user(organization.getUser())
                .address(organization.getAddress())
                .name(organization.getName())
                .status(String.valueOf(organization.getStatus()))
                .isActive(organization.getIsActive())
                .taxNumber(organization.getTaxNumber())
                .build();

    }

    @Override
    public Organization mapDto(OrganizationDTO organizationDTO) {
        return Organization.builder()
                .id(organizationDTO.getId())
                .user(organizationDTO.getUser())
                .address(organizationDTO.getAddress())
                .name(organizationDTO.getName())
                .taxNumber(organizationDTO.getTaxNumber())
                .build();
    }
}
