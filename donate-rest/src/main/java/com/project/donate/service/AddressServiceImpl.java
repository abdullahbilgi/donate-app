package com.project.donate.service;


import com.project.donate.dto.AddressDTO;
import com.project.donate.mapper.AddressMapper;
import com.project.donate.model.Address;
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


    @Override
    public List<AddressDTO> getAllAddress() {
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::map)
                .orElseThrow(() -> new RuntimeException("Address not found id: " + id));
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = addressMapper.mapDto(addressDTO);
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

    private AddressDTO saveAndMap(Address address) {
        Address savedAddress = addressRepository.save(address);

        return addressMapper.map(savedAddress);
    }
}
