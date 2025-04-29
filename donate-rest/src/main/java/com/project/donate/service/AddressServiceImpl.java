package com.project.donate.service;


import com.project.donate.dto.AddressDTO;
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
    public List<AddressDTO> getAllAddress() {
        return addressRepository.findAllByIsActiveTrue()
                .stream()
                .map(addressMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        log.info("{} looked address with id: {}", GeneralUtil.extractUsername(), id);
        return addressMapper.map(getAddressEntityById(id));
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = addressMapper.mapDto(addressDTO);
        Region region = regionService.getRegionEntityById(addressDTO.getRegionId());
        address.setRegion(region);
        return saveAndMap(address,"save");
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        getAddressById(id);
        Region region = regionService.getRegionEntityById(addressDTO.getRegionId());
        Address savingAddress = addressMapper.mapDto(addressDTO);
        savingAddress.setRegion(region);
        savingAddress.setId(id);
        return saveAndMap(savingAddress, "update");
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = getAddressEntityById(id);
        address.setIsActive(false);
        log.info("{} Deleted address: {}", GeneralUtil.extractUsername(), address);
        addressRepository.save(address);
    }

    @Override
    public Address getAddressEntityById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{} Address not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Address not found id: " + id);}
                );
    }

    private AddressDTO saveAndMap(Address address, String status) {
        Address savedAddress = addressRepository.save(address);

        if (status.equals("save")) {
            log.info("{} Created address: {}", GeneralUtil.extractUsername(), address);
        } else{
            log.info("{} Updated address: {}", GeneralUtil.extractUsername(), address);
        }
        return addressMapper.map(savedAddress);
    }
}
