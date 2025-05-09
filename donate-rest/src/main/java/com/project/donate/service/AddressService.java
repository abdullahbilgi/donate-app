package com.project.donate.service;

import com.project.donate.dto.Request.AddressRequest;
import com.project.donate.dto.Response.AddressResponse;
import com.project.donate.model.Address;

import java.util.List;

public interface AddressService {
    AddressResponse createAddress(AddressRequest request);

    AddressResponse updateAddress(Long id,AddressRequest request);

    List<AddressResponse> getAllAddress();

    AddressResponse getAddressById(Long id);

    void deleteAddress(Long id);

    Address getAddressEntityById(Long id);

    Address createAddressEntity(AddressRequest address);

    Address saveAddress(Address address);
}
