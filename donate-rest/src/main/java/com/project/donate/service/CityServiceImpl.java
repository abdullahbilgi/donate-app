package com.project.donate.service;

import com.project.donate.dto.CityDTO;
import com.project.donate.dto.Request.CityRequest;
import com.project.donate.dto.Response.CityResponse;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.CityMapper;
import com.project.donate.model.City;
import com.project.donate.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;


    @Override
    public List<CityResponse> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(cityMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CityResponse getCityById(Long id) {
       return cityMapper.mapToDto(getCityEntityById(id));
    }

    @Override
    public CityResponse createCity(CityRequest request) {
        City city = cityMapper.mapToEntity(request);
        return saveAndMap(city);
    }

    @Override
    public CityResponse updateCity(Long id, CityRequest cityRequest) {

        getCityById(id);
        City savingCity = cityMapper.mapToEntity(cityRequest);
        savingCity.setId(id);
        return saveAndMap(savingCity);
    }

    @Override
    public void deleteCity(Long id) {
        getCityById(id);
        cityRepository.deleteById(id);
    }

    @Override
    public City getCityEntityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found id: " + id));
    }

    private CityResponse saveAndMap(City city) {
        City savedCity = cityRepository.save(city);
        return cityMapper.mapToDto(savedCity);
    }
}