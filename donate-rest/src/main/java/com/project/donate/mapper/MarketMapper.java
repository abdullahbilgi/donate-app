package com.project.donate.mapper;

import com.project.donate.dto.MarketDTO;
import com.project.donate.model.Market;
import org.springframework.stereotype.Component;

@Component
public class MarketMapper implements ObjectMapper<Market, MarketDTO>{
    @Override
    public MarketDTO map(Market market) {
        return MarketDTO.builder()
                .id(market.getId())
                .user(market.getUser())
                .address(market.getAddress())
                .name(market.getName())
                .status(String.valueOf(market.getStatus()))
                .isActive(market.getIsActive())
                .products(market.getProducts())
                .taxNumber(market.getTaxNumber())
                .build();

    }

    @Override
    public Market mapDto(MarketDTO marketDTO) {
        return Market.builder()
                .id(marketDTO.getId())
                .user(marketDTO.getUser())
                .address(marketDTO.getAddress())
                .name(marketDTO.getName())
                .products(marketDTO.getProducts())
                .taxNumber(marketDTO.getTaxNumber())
                .build();
    }
}
