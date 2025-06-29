package com.project.donate.service;

import com.project.donate.dto.Request.ApplyOrganizationRequest;
import com.project.donate.dto.Request.OrganizationRequest;
import com.project.donate.dto.Response.OrganizationResponse;
import com.project.donate.enums.Role;
import com.project.donate.enums.Status;
import com.project.donate.exception.OutOfStockException;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.OrganizationMapper;
import com.project.donate.model.*;
import com.project.donate.records.ProductItem;
import com.project.donate.repository.OrganizationRepository;
import com.project.donate.repository.ProductRepository;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private final AddressService addressService;
    private final ProductRepository productRepository;
    private final UserService userService;

    private final CityService cityService;
    private final RegionService regionService;

    @Override
    public List<OrganizationResponse> getAllOrganization() {
        return organizationRepository.findByIsActiveTrue()
                .stream().map(organizationMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationResponse getOrganizationById(Long id) {
        return organizationMapper.mapToDto(getOrganizationEntityById(id));
    }

    @Override
    public Organization getOrganizationEntityById(Long id) {
        log.info("{} - looked organization with id: {}", GeneralUtil.extractUsername(), id);
        return organizationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{} organization not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Organization not found: " + id);
                });
    }

/**
    @Override
    public OrganizationResponse createOrganization(OrganizationRequest organizationDTO) {
        Organization organization = organizationMapper.mapToEntity(organizationDTO);
        Address address = addressService.createAddressEntity(organizationDTO.getAddress());
        addressService.saveAddress(address);
        User user = userService.getUserEntityById(organizationDTO.getUserId());
        organization.setUser(user);
        organization.setAddress(address);
        return saveAndMap(organization, "save");
    }
 **/

    @Override
    public OrganizationResponse updateOrganization(Long id, OrganizationRequest request) {
        Organization organization = getOrganizationEntityById(id);
        organizationMapper.mapUpdateAddressRequestToOrganization(request, organization);
        Address address = addressService.createAddressEntity(request.getAddress());
        addressService.saveAddress(address);
        organization.setAddress(address);
        return saveAndMap(organization, "update");
    }

    @Override
    public void enabledOrganization(Long id) {
        Organization organization = getOrganizationEntityById(id);
        organization.setStatus(Status.APPROVED);

        log.info("{} enabled organization", GeneralUtil.extractUsername());
        organizationRepository.save(organization);

    }

    /**

    @Override
    public OrganizationResponse assignProduct(Long organizationId, ProductItem newItem) {
        Organization organization = getOrganizationEntityById(organizationId);

        List<ProductItem> existingItems = organization.getProductItems();
        if (existingItems == null) {
            existingItems = new ArrayList<>();
        }

        Product product = productRepository.findById(newItem.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found id: " + newItem.productId()));

        if (!product.getIsActive()) {
            throw new RuntimeException("Product is not active");
        }

        if (product.getQuantity() < newItem.quantity()) {
            throw new OutOfStockException("Not enough stock for product id: " + newItem.productId());
        }

        product.setQuantity(product.getQuantity() - newItem.quantity());
        productRepository.save(product);

        boolean found = false;
        for (int i = 0; i < existingItems.size(); i++) {
            ProductItem existingItem = existingItems.get(i);
            if (existingItem.productId().equals(newItem.productId())) {
                int updatedQuantity = existingItem.quantity() + newItem.quantity();
                existingItems.set(i, new ProductItem(existingItem.productId(), updatedQuantity));
                found = true;
                break;
            }
        }

        if (!found) {
            existingItems.add(newItem);
        }

        organization.setProductItems(existingItems);
        organizationRepository.save(organization);
        return organizationMapper.mapToDto(organization);
    }
     **/

    /**

    @Override
    public OrganizationResponse removeProduct(Long organizationId, Long productId, int quantityToRemove) {
        Organization organization = getOrganizationEntityById(organizationId);
        List<ProductItem> existingItems = organization.getProductItems();

        if (existingItems == null || existingItems.isEmpty()) {
            throw new ResourceNotFoundException("No products assigned to this organization");
        }

        boolean removed = false;
        for (int i = 0; i < existingItems.size(); i++) {
            ProductItem item = existingItems.get(i);
            if (item.productId().equals(productId)) {
                if (item.quantity() <= quantityToRemove) {
                    existingItems.remove(i); // tümünü sil
                } else {
                    existingItems.set(i, new ProductItem(productId, item.quantity() - quantityToRemove));
                }

                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found id: " + productId));
                product.setQuantity(product.getQuantity() + quantityToRemove);
                productRepository.save(product);

                removed = true;
                break;
            }
        }

        if (!removed) {
            throw new ResourceNotFoundException("Product not assigned to this organization");
        }

        organization.setProductItems(existingItems);
        organizationRepository.save(organization);
        return organizationMapper.mapToDto(organization);
    }

     **/





    @Override
    public void deleteOrganization(Long id) {
        Organization organization = getOrganizationEntityById(id);
        organization.setIsActive(false);
        log.info("{} Deleted organization: {}", GeneralUtil.extractUsername(), organization);
        organizationRepository.save(organization);

    }

    @Override
    public Page<OrganizationResponse> getOrganizationsByStatusPageable(Status status, Pageable pageable) {
        return organizationRepository.getOrganizationsByStatusAndIsActiveTrue(status, pageable)
                .map(organizationMapper::mapToDto);
    }

    @Override
    public List<OrganizationResponse> getOrganizationsByStatus(Status status) {
        return organizationRepository.getOrganizationsByStatusAndIsActiveTrue(status)
                .stream().map(organizationMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationResponse applyOrganization(ApplyOrganizationRequest request) {
        Organization organization = organizationMapper.mapToEntity(request);
        User user = userService.getUserEntityById(request.getUserId());
        organization.setUser(user);
        organization.setStatus(Status.PENDING);

        Address address = new Address();
        City city = cityService.getCityEntityByName(request.getCityName());
        Region region = regionService.getRegionEntityByName(request.getRegionName(),city.getName());

        //Address doldurma

        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());
        address.setRegion(region);
        address.setName(request.getDisplayName());
        address.setZipCode(request.getZipCode());
        addressService.saveAddress(address);
        organization.setAddress(address);

        organizationRepository.save(organization);

        return organizationMapper.mapToDto(organization);
    }

    @Override
    public OrganizationResponse confirmOrganization(Long organizationId) {
        Organization organization = getOrganizationEntityById(organizationId);
        organization.setStatus(Status.APPROVED);
        organization.setIsActive(true);
        organizationRepository.save(organization);
        User user = userService.getUserEntityById(organization.getUser().getId());
        user.setRole(Role.BENEFACTOR);
        userService.save(user);
        return organizationMapper.mapToDto(organization);
    }

    @Override
    public OrganizationResponse rejectOrganization(Long organizationId) {
        Organization organization = getOrganizationEntityById(organizationId);
        organization.setStatus(Status.REJECTED);
        organizationRepository.save(organization);
        return organizationMapper.mapToDto(organization);
    }

    private OrganizationResponse saveAndMap(Organization organization, String status) {
        Organization savedOrganization = organizationRepository.save(organization);

        switch (status) {
            case "save" -> log.info("{} Created organization: {}", GeneralUtil.extractUsername(), organization);
            case "update" -> log.info("{} Updated organization: {}", GeneralUtil.extractUsername(), organization);
        }

        return organizationMapper.mapToDto(savedOrganization);
    }
}
