package com.project.donate.mapper;

import com.project.donate.dto.Request.MarketRequest;
import com.project.donate.dto.Response.AddressResponse;
import com.project.donate.dto.Response.MarketResponse;
import com.project.donate.dto.Response.ProductResponse;
import com.project.donate.dto.Response.UserResponse;
import com.project.donate.model.Market;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MarketMapper {
    private final AddressMapper addressMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    public MarketResponse mapToDto(Market market) {
        AddressResponse addressResponse = addressMapper.mapToDto(market.getAddress());

        Set<ProductResponse> productResponses = market.getProducts().stream()
                .map(productMapper::mapToDto)
                .collect(Collectors.toSet());

        UserResponse userResponse = userMapper.userToUserDto(market.getUser());

        return MarketResponse.builder()
                .id(market.getId())
                .user(userResponse)
                .address(addressResponse)
                .name(market.getName())
                .status(String.valueOf(market.getStatus()))
                //.products(productResponses)
                .taxNumber(market.getTaxNumber())
                .build();
    }

    public Market mapToEntity(MarketRequest request) {
        return Market.builder()
                .id(request.getId())
                .name(request.getName())
                .taxNumber(request.getTaxNumber())
                .build();
    }

    public void mapUpdateAddressRequestToMarket(MarketRequest request,Market market) {
        market.setName(request.getName());
        market.setTaxNumber(request.getTaxNumber());
    }
}
