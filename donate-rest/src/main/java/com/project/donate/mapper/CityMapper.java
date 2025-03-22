package com.project.donate.mapper;

import com.project.donate.dto.CityDTO;
import com.project.donate.model.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper implements ObjectMapper<City, CityDTO> {

    @Override
    public CityDTO map(City city) {
        return CityDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }

    @Override
    public City mapDto(CityDTO cityDTO) {
        return City.builder()
                .id(cityDTO.getId())
                .name(cityDTO.getName())
                .build();
    }
}