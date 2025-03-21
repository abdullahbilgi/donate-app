package com.project.donate.service;


import com.project.donate.dto.RegionDTO;
import com.project.donate.mapper.RegionMapper;
import com.project.donate.model.Region;
import com.project.donate.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;


    @Override
    public List<RegionDTO> getAllCities() {
        return regionRepository.findAll()
                .stream()
                .map(regionMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public RegionDTO getRegionById(Long id) {
        return regionRepository.findById(id)
                .map(regionMapper::map)
                .orElseThrow(() -> new RuntimeException("Region not found id: " + id));
    }

    @Override
    public RegionDTO createRegion(RegionDTO regionDTO) {
        Region region = regionMapper.mapDto(regionDTO);
        return saveAndMap(region);
    }

    @Override
    public RegionDTO updateRegion(Long id, RegionDTO regionDTO) {

        getRegionById(id);
        Region savingRegion = regionMapper.mapDto(regionDTO);
        savingRegion.setId(id);

        return saveAndMap(savingRegion);
    }

    @Override
    public void deleteRegion(Long id) {

        getRegionById(id);
        regionRepository.deleteById(id);

    }

    private RegionDTO saveAndMap(Region region) {
        Region savedRegion = regionRepository.save(region);

        return regionMapper.map(savedRegion);
    }
}
