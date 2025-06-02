package com.project.donate.repository;


import com.project.donate.enums.Status;
import com.project.donate.model.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {
    List<Market> findByIsActiveTrue();

    // is active sadece soft delete icin kullanılacak geri kalanlar status üzerinden olacak
    List<Market> getMarketsByStatusAndIsActiveTrue(Status status);

    Page<Market> getMarketsByStatusAndIsActiveTrue(Status status, Pageable pageable);

    List<Market> findAllByUserIdAndIsActiveTrue(Long userId);
}
