package com.project.donate.service;

import com.project.donate.dto.OrganizationDTO;
import com.project.donate.enums.Status;
import com.project.donate.mapper.OrganizationMapper;
import com.project.donate.model.Organization;
import com.project.donate.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;


    @Override
    public List<OrganizationDTO> getAllOrganization() {
        return organizationRepository.findAll()
                .stream().map(organizationMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationDTO getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .map(organizationMapper::map)
                .orElseThrow(() -> new RuntimeException("Organization not found: " + id));
    }


    @Override
    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
        Organization organization = organizationMapper.mapDto(organizationDTO);
        return saveAndMap(organization);
    }

    @Override
    public OrganizationDTO updateOrganization(Long id, OrganizationDTO organizationDTO) {
        OrganizationDTO dto = getOrganizationById(id);
        Organization savingOrganization = organizationMapper.mapDto(organizationDTO);
        savingOrganization.setId(id);
        savingOrganization.setStatus(Status.valueOf(dto.getStatus()));
        savingOrganization.setIsActive(dto.getIsActive());
        return saveAndMap(savingOrganization);
    }

    @Override
    public void enabledOrganization(Long id) {
        OrganizationDTO organizationDTO = getOrganizationById(id);
        Organization organization = organizationMapper.mapDto(organizationDTO);
        organization.setStatus(Status.APPROVED);
        organization.setIsActive(true);
        organizationRepository.save(organization);

    }


    @Override
    public void deleteOrganization(Long id) {
        OrganizationDTO organizationDTO = getOrganizationById(id);
        Organization organization = organizationMapper.mapDto(organizationDTO);
        organization.setIsActive(false);
        saveAndMap(organization);

    }

    private OrganizationDTO saveAndMap(Organization organization) {
        Organization savedOrganization = organizationRepository.save(organization);

        return organizationMapper.map(savedOrganization);
    }
}
