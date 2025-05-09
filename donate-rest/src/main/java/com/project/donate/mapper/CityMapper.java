package com.project.donate.mapper;


import com.project.donate.dto.Request.CityRequest;
import com.project.donate.dto.Response.CityResponse;
import com.project.donate.model.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper  {

    public CityResponse mapToDto(City city) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }

    public City mapToEntity(CityRequest cityDTO) {
        return City.builder()
                .id(cityDTO.getId())
                .name(cityDTO.getName())
                .build();
    }
}