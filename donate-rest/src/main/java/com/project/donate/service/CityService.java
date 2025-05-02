package com.project.donate.service;

import com.project.donate.dto.CityDTO;
import com.project.donate.dto.Request.CityRequest;
import com.project.donate.dto.Response.CityResponse;
import com.project.donate.model.City;

import java.util.List;

public interface CityService {
    CityResponse createCity(CityRequest request);

    CityResponse updateCity(Long id,CityRequest request);

    List<CityResponse> getAllCities();

    CityResponse getCityById(Long id);

    void deleteCity(Long id);

    City getCityEntityById(Long id);
}
