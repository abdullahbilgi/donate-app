package com.project.donate.repository;


import com.project.donate.model.Product;
import com.project.donate.model.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByNameAndCityName(String regionName, String cityName);
}
