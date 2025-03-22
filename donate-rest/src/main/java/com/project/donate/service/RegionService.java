package com.project.donate.service;

import com.project.donate.dto.RegionDTO;

import java.util.List;

public interface RegionService {
    RegionDTO createRegion(RegionDTO regionDTO);

    RegionDTO updateRegion(Long id,RegionDTO regionDTO);

    List<RegionDTO> getAllCities();

    RegionDTO getRegionById(Long id);

    void deleteRegion(Long id);
}
