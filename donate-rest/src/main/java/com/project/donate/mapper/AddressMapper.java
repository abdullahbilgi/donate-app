package com.project.donate.mapper;

import com.project.donate.dto.AddressDTO;
import com.project.donate.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper implements ObjectMapper<Address, AddressDTO> {
    @Override
    public AddressDTO map(Address address) {

        return AddressDTO.builder()
                .id(address.getId())
                .name(address.getName())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .zipCode(address.getZipCode())
                .region(address.getRegion())
                .build();
    }

    @Override
    public Address mapDto(AddressDTO addressDTO) {
        return Address.builder()
                .id(addressDTO.getId())
                .name(addressDTO.getName())
                .latitude(addressDTO.getLatitude())
                .longitude(addressDTO.getLongitude())
                .zipCode(addressDTO.getZipCode())
                .region(addressDTO.getRegion())
                .build();
    }
}
