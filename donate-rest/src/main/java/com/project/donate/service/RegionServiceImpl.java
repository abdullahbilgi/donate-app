package com.project.donate.service;


import com.project.donate.dto.RegionDTO;
import com.project.donate.dto.Request.RegionRequest;
import com.project.donate.dto.Response.RegionResponse;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.RegionMapper;
import com.project.donate.model.City;
import com.project.donate.model.Region;
import com.project.donate.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;
    private final CityService cityService;


    @Override
    public List<RegionResponse> getAllCities() {
        return regionRepository.findAll()
                .stream()
                .map(regionMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RegionResponse getRegionById(Long id) {
       return regionMapper.mapToDto(getRegionEntityById(id));
    }

    @Override
    public RegionResponse createRegion(RegionRequest request) {
        Region region = regionMapper.mapToEntity(request);
        City city = cityService.getCityEntityById(request.getCityId());
        region.setCity(city);
        return saveAndMap(region);
    }

    @Override
    public RegionResponse updateRegion(Long id, RegionRequest request) {

        Region region = getRegionEntityById(id);
        City city = cityService.getCityEntityById(request.getCityId());
        region.setCity(city);
        return saveAndMap(region);
    }

    @Override
    public void deleteRegion(Long id) {
        getRegionById(id);
        regionRepository.deleteById(id);
    }

    @Override
    public Region getRegionEntityById(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found id: " + id));
    }

    private RegionResponse saveAndMap(Region region) {
        Region savedRegion = regionRepository.save(region);
        return regionMapper.mapToDto(savedRegion);
    }
}
