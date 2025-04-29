package com.project.donate.service;

import com.project.donate.dto.CityDTO;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.CityMapper;
import com.project.donate.model.City;
import com.project.donate.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;


    @Override
    public List<CityDTO> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(cityMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public CityDTO getCityById(Long id) {
        return cityMapper.map(getCityEntityById(id));
    }

    @Override
    public CityDTO createCity(CityDTO cityDTO) {
        City city = cityMapper.mapDto(cityDTO);

        return saveAndMap(city);
    }

    @Override
    public CityDTO updateCity(Long id, CityDTO cityDTO) {

        getCityById(id);
        City savingCity = cityMapper.mapDto(cityDTO);
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

    private CityDTO saveAndMap(City city) {
        City savedCity = cityRepository.save(city);

        return cityMapper.map(savedCity);
    }
}