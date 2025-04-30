package com.project.donate.mapper;

import com.project.donate.dto.RegionDTO;
import com.project.donate.dto.Request.RegionRequest;
import com.project.donate.dto.Response.CityResponse;
import com.project.donate.dto.Response.RegionResponse;
import com.project.donate.model.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegionMapper {
    private final CityMapper cityMapper;
    public RegionResponse mapToDto(Region region) {
        CityResponse cityResponse = cityMapper.mapToDto(region.getCity());
        return RegionResponse.builder()
                .id(region.getId())
                .name(region.getName())
                .city(cityResponse)
                .build();
    }

    public Region mapToEntity(RegionRequest request) {
        return Region.builder()
                .id(request.getId())
                .name(request.getName())
                .build();
    }
}
