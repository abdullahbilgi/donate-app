package com.project.donate.service;


import com.project.donate.dto.AddressDTO;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.AddressMapper;
import com.project.donate.model.Address;
import com.project.donate.model.Region;
import com.project.donate.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final RegionService regionService;


    @Override
    public List<AddressDTO> getAllAddress() {
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        return addressMapper.map(getAddressEntityById(id));
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = addressMapper.mapDto(addressDTO);
        Region region = regionService.getRegionEntityById(addressDTO.getRegionId());
        address.setRegion(region);
        return saveAndMap(address);
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {

        getAddressById(id);
        Address savingAddress = addressMapper.mapDto(addressDTO);
        savingAddress.setId(id);

        return saveAndMap(savingAddress);
    }

    @Override
    public void deleteAddress(Long id) {

        getAddressById(id);
        addressRepository.deleteById(id);

    }

    @Override
    public Address getAddressEntityById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found id: " + id));
    }

    private AddressDTO saveAndMap(Address address) {
        Address savedAddress = addressRepository.save(address);

        return addressMapper.map(savedAddress);
    }
}
