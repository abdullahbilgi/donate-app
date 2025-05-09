package com.project.donate.service;

import com.project.donate.dto.Request.RegionRequest;
import com.project.donate.dto.Response.RegionResponse;
import com.project.donate.model.Region;

import java.util.List;

public interface RegionService {
    RegionResponse createRegion(RegionRequest request);

    RegionResponse updateRegion(Long id,RegionRequest request);

    List<RegionResponse> getAllCities();

    RegionResponse getRegionById(Long id);

    void deleteRegion(Long id);

    Region getRegionEntityById(Long id);
}
