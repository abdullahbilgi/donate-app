package com.project.donate.mapper;

import com.project.donate.dto.RegionDTO;
import com.project.donate.model.Region;
import org.springframework.stereotype.Component;

@Component
public class RegionMapper implements ObjectMapper<Region, RegionDTO> {
    @Override
    public RegionDTO map(Region region) {
        return RegionDTO.builder()
                .id(region.getId())
                .name(region.getName())
                .city(region.getCity())
                .build();
    }

    @Override
    public Region mapDto(RegionDTO regionDTO) {
        return Region.builder()
                .id(regionDTO.getId())
                .name(regionDTO.getName())
                .city(regionDTO.getCity())
                .build();
    }
}
