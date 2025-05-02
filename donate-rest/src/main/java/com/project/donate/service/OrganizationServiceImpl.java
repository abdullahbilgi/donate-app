package com.project.donate.service;

import com.project.donate.dto.OrganizationDTO;
import com.project.donate.enums.Status;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.OrganizationMapper;
import com.project.donate.model.Organization;
import com.project.donate.repository.OrganizationRepository;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
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
        log.info("{} - looked organization with id: {}", GeneralUtil.extractUsername(), id);
        return organizationRepository.findById(id)
                .map(organizationMapper::map)
                .orElseThrow(() -> {
                    log.error("{} organization not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Organization not found: " + id);
                });
    }


    @Override
    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
        Organization organization = organizationMapper.mapDto(organizationDTO);
        return saveAndMap(organization, "save");
    }

    @Override
    public OrganizationDTO updateOrganization(Long id, OrganizationDTO organizationDTO) {
        OrganizationDTO dto = getOrganizationById(id);
        Organization savingOrganization = organizationMapper.mapDto(organizationDTO);
        savingOrganization.setId(id);
        savingOrganization.setStatus(Status.valueOf(dto.getStatus()));
        savingOrganization.setIsActive(dto.getIsActive());
        return saveAndMap(savingOrganization, "update");
    }

    @Override
    public void enabledOrganization(Long id) {
        OrganizationDTO organizationDTO = getOrganizationById(id);
        Organization organization = organizationMapper.mapDto(organizationDTO);
        organization.setStatus(Status.APPROVED);
        organization.setIsActive(true);

        log.info("{} enabled organization", GeneralUtil.extractUsername());
        organizationRepository.save(organization);

    }


    @Override
    public void deleteOrganization(Long id) {
        OrganizationDTO organizationDTO = getOrganizationById(id);
        Organization organization = organizationMapper.mapDto(organizationDTO);
        organization.setIsActive(false);
        saveAndMap(organization, "delete");

    }

    private OrganizationDTO saveAndMap(Organization organization, String status) {
        Organization savedOrganization = organizationRepository.save(organization);

        switch (status) {
            case "save" -> log.info("{} Created organization: {}", GeneralUtil.extractUsername(), organization);
            case "update" -> log.info("{} Updated organization: {}", GeneralUtil.extractUsername(), organization);
            case "delete" -> log.info("{} Deleted organization: {}", GeneralUtil.extractUsername(), organization);
        }

        return organizationMapper.map(savedOrganization);
    }
}
