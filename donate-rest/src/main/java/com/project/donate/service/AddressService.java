package com.project.donate.service;

import com.project.donate.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO);

    AddressDTO updateAddress(Long id,AddressDTO addressDTO);

    List<AddressDTO> getAllAddress();

    AddressDTO getAddressById(Long id);

    void deleteAddress(Long id);
}
