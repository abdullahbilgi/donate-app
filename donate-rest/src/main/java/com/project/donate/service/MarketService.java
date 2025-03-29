package com.project.donate.service;

import com.project.donate.dto.MarketDTO;

import java.util.List;

public interface MarketService {

    MarketDTO createMarket(MarketDTO marketDTO);

    MarketDTO updateMarket(Long id, MarketDTO marketDTO);

    List<MarketDTO> getAllMarket();

    MarketDTO getMarketById(Long id);

    void assignProduct(Long marketId, Long productId);

    void enabledMarket(Long id);

    void deleteMarket(Long id);


}
