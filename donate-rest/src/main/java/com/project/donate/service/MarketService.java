package com.project.donate.service;

import com.project.donate.dto.MarketDTO;
import com.project.donate.dto.ProductDTO;
import com.project.donate.enums.Status;
import com.project.donate.model.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MarketService {

    MarketDTO createMarket(MarketDTO marketDTO);

    Market getMarketEntityById(Long id);

    MarketDTO updateMarket(Long id, MarketDTO marketDTO);

    List<MarketDTO> getAllMarket();

    MarketDTO getMarketById(Long id);

    void assignProduct(Long marketId, Long productId);

    void enabledMarket(Long id);

    void deleteMarket(Long id);

    Page<MarketDTO> getMarketsByStatusPageable(Status status ,Pageable pageable);

    List<MarketDTO> getMarketsByStatus(Status status);
}
