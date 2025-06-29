package com.project.donate.mapper;


import com.project.donate.dto.Request.ApplyOrganizationRequest;
import com.project.donate.dto.Request.MarketRequest;
import com.project.donate.dto.Request.OrganizationRequest;
import com.project.donate.dto.Response.*;
import com.project.donate.model.Market;
import com.project.donate.model.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrganizationMapper{

    private final AddressMapper addressMapper;
    private final UserMapper userMapper;

    public OrganizationResponse mapToDto(Organization organization) {
        AddressResponse addressResponse = addressMapper.mapToDto(organization.getAddress());

        UserResponse userResponse = userMapper.userToUserDto(organization.getUser());

        return OrganizationResponse.builder()
                .id(organization.getId())
                .user(userResponse)
                .address(addressResponse)
                .name(organization.getName())
                .status(String.valueOf(organization.getStatus()))
                //.productItems(organization.getProductItems())
                .taxNumber(organization.getTaxNumber())
                .build();
    }

    public Organization mapToEntity(ApplyOrganizationRequest request) {
        return Organization.builder()
                .name(request.getName())
                .taxNumber(request.getTaxNumber())
                .build();
    }

    public void mapUpdateAddressRequestToOrganization(OrganizationRequest request,Organization organization) {
        organization.setName(request.getName());
        organization.setTaxNumber(request.getTaxNumber());
    }
}
