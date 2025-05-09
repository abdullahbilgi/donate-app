package com.project.donate.mapper;


import com.project.donate.dto.Request.AddressRequest;
import com.project.donate.dto.Response.AddressResponse;
import com.project.donate.dto.Response.RegionResponse;
import com.project.donate.model.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper  {
    private final RegionMapper regionMapper;

    public AddressResponse mapToDto(Address address) {
        RegionResponse regionResponse = regionMapper.mapToDto(address.getRegion());
        return AddressResponse.builder()
                .id(address.getId())
                .name(address.getName())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .zipCode(address.getZipCode())
                .region(regionResponse)
                .build();
    }

    public Address mapToEntity(AddressRequest addressRequest) {
        return Address.builder()
                .id(addressRequest.getId())
                .name(addressRequest.getName())
                .latitude(addressRequest.getLatitude())
                .longitude(addressRequest.getLongitude())
                .zipCode(addressRequest.getZipCode())
                .build();
    }

    public Address mapUpdateAddressRequestToAddress(AddressRequest addressRequest,Address address) {
        address.setName(addressRequest.getName());
        address.setLatitude(addressRequest.getLatitude());
        address.setLongitude(addressRequest.getLongitude());
        address.setZipCode(addressRequest.getZipCode());
        return address;
    }

}
