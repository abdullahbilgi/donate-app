package com.project.donate.service;

import com.project.donate.dto.Request.MarketRequest;
import com.project.donate.dto.Response.MarketResponse;
import com.project.donate.enums.Status;
import com.project.donate.model.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MarketService {

    MarketResponse createMarket(MarketRequest request);

    Market getMarketEntityById(Long id);

    MarketResponse updateMarket(Long id, MarketRequest request);

    List<MarketResponse> getAllMarket();

    MarketResponse getMarketById(Long id);

    //void assignProduct(Long marketId, Long productId);

    void enabledMarket(Long id);

    void deleteMarket(Long id);

    Page<MarketResponse> getMarketsByStatusPageable(Status status ,Pageable pageable);

    List<MarketResponse> getMarketsByStatus(Status status);
}
