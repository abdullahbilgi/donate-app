package com.project.donate.service;

import com.project.donate.dto.AddressDTO;
import com.project.donate.dto.MarketDTO;
import com.project.donate.dto.ProductDTO;
import com.project.donate.dto.Request.MarketRequest;
import com.project.donate.dto.Response.MarketResponse;
import com.project.donate.enums.Status;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.AddressMapper;
import com.project.donate.mapper.MarketMapper;
import com.project.donate.mapper.ProductMapper;
import com.project.donate.model.Address;
import com.project.donate.model.Market;
import com.project.donate.model.Product;
import com.project.donate.model.User;
import com.project.donate.repository.AddressRepository;
import com.project.donate.repository.MarketRepository;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;
    private final MarketMapper marketMapper;
    private final ProductService productService;
    private final AddressService addressService;
    private final UserService userService;



    @Override
    public List<MarketResponse> getAllMarket() {
        return marketRepository.findByIsActiveTrue()
                .stream().map(marketMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MarketResponse getMarketById(Long id) {
        return marketMapper.mapToDto(getMarketEntityById(id));
    }


    @Override
    public MarketResponse createMarket(MarketRequest marketDTO) {
        Market market = marketMapper.mapToEntity(marketDTO);
        Address address = addressService.createAddressEntity(marketDTO.getAddress());
        addressService.saveAddress(address);
        User user = userService.getUserEntityById(marketDTO.getUserId());
        market.setUser(user);
        market.setAddress(address);
        return saveAndMap(market,"save");
    }

    @Override
    public Market getMarketEntityById(Long id) {
        log.info("{} - looked market with id: {}", GeneralUtil.extractUsername(), id);

        return marketRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{} market not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Market not found: " + id);
                });
    }

    @Override
    public MarketResponse updateMarket(Long id, MarketRequest request) {
        Market market = getMarketEntityById(id);
        marketMapper.mapUpdateAddressRequestToMarket(request, market);
        Address address = addressService.createAddressEntity(request.getAddress());
        addressService.saveAddress(address);
        market.setAddress(address);
        return saveAndMap(market, "update");
    }

    @Override
    public void enabledMarket(Long id) {
        Market market = getMarketEntityById(id);
        market.setStatus(Status.APPROVED);

        log.info("{} enabled market", GeneralUtil.extractUsername());
        marketRepository.save(market);
    }

    @Override
    public void assignProduct(Long marketId, Long productId) {

        Market market = getMarketEntityById(marketId);
        Product product = productService.getProductEntityById(productId);
        market.getProducts().add(product);
        market.setStatus(Status.APPROVED);

        log.info("{} assigned product", GeneralUtil.extractUsername());
        marketRepository.save(market);
    }

    @Override
    public void deleteMarket(Long id) {
        Market market = getMarketEntityById(id);
        market.setIsActive(false);
        log.info("{} Deleted address: {}", GeneralUtil.extractUsername(), market);
        marketRepository.save(market);
    }

    @Override
    public Page<MarketResponse> getMarketsByStatusPageable(Status status, Pageable pageable) {
        return marketRepository.getMarketsByStatusAndIsActiveTrue(status,pageable)
                .map(marketMapper::mapToDto);
    }

    @Override
    public List<MarketResponse> getMarketsByStatus(Status status) {
        return marketRepository.getMarketsByStatusAndIsActiveTrue(status)
                .stream().map(marketMapper::mapToDto)
                .collect(Collectors.toList());
    }

    private MarketResponse saveAndMap(Market market, String status) {
        Market savedMarket = marketRepository.save(market);

        if (status.equals("save")) {
            log.info("{} Created market: {}", GeneralUtil.extractUsername(), market);
        } else {
            log.info("{} Updated market: {}", GeneralUtil.extractUsername(), market);
        }
        return marketMapper.mapToDto(savedMarket);
    }
}
