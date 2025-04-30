package com.project.donate.service;


import com.project.donate.dto.AddressDTO;
import com.project.donate.dto.Request.AddressRequest;
import com.project.donate.dto.Response.AddressResponse;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.AddressMapper;
import com.project.donate.model.Address;
import com.project.donate.model.Region;
import com.project.donate.repository.AddressRepository;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final RegionService regionService;


    @Override
    public List<AddressResponse> getAllAddress() {
        return addressRepository.findAllByIsActiveTrue()
                .stream()
                .map(addressMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse getAddressById(Long id) {
        log.info("{} looked address with id: {}", GeneralUtil.extractUsername(), id);
        return addressMapper.mapToDto(getAddressEntityById(id));
    }

    @Override
    public AddressResponse createAddress(AddressRequest addressRequest) {
        return saveAndMap(createAddressEntity(addressRequest),"save");
    }

    @Override
    public Address createAddressEntity(AddressRequest request) {
        Address address = addressMapper.mapToEntity(request);
        Region region = regionService.getRegionEntityById(request.getRegionId());
        address.setRegion(region);
        return address;
    }

    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public AddressResponse updateAddress(Long id, AddressRequest request) {
        Address address = getAddressEntityById(id);
        address = addressMapper.mapUpdateAddressRequestToAddress(request,address);
        Region region = regionService.getRegionEntityById(request.getRegionId());
        address.setRegion(region);
        return saveAndMap(address, "update");
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = getAddressEntityById(id);
        address.setIsActive(false);
        log.info("{} Deleted address: {}", GeneralUtil.extractUsername(), address);
        saveAddress(address);
    }

    @Override
    public Address getAddressEntityById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{} Address not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Address not found id: " + id);}
                );
    }



    private AddressResponse saveAndMap(Address address, String status) {
        Address savedAddress = saveAddress(address);

        if (status.equals("save")) {
            log.info("{} Created address: {}", GeneralUtil.extractUsername(), address);
        } else{
            log.info("{} Updated address: {}", GeneralUtil.extractUsername(), address);
        }
        return addressMapper.mapToDto(savedAddress);
    }
}
