package com.project.donate.service;

import com.project.donate.dto.CityDTO;
import com.project.donate.model.City;

import java.util.List;

public interface CityService {
    CityDTO createCity(CityDTO cityDTO);

    CityDTO updateCity(Long id,CityDTO cityDTO);

    List<CityDTO> getAllCities();

    CityDTO getCityById(Long id);

    void deleteCity(Long id);

    City getCityEntityById(Long id);
}
