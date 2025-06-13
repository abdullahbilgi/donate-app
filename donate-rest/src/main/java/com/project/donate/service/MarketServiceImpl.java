package com.project.donate.service;

import com.project.donate.dto.Request.MarketRequest;
import com.project.donate.dto.Response.MarketResponse;
import com.project.donate.enums.Status;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.MarketMapper;
import com.project.donate.model.*;
import com.project.donate.repository.MarketRepository;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;
    private final MarketMapper marketMapper;
    private final AddressService addressService;
    private final UserService userService;
    private final CityService cityService;
    private final RegionService regionService;


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
        Address address = new Address();
        City city = cityService.getCityEntityByName(marketDTO.getCityName());
        Region region = regionService.getRegionEntityByName(marketDTO.getRegionName(),city.getName());
        //Address doldurma
        address.setLatitude(marketDTO.getLatitude());
        address.setLongitude(marketDTO.getLongitude());
        address.setRegion(region);
        address.setName(marketDTO.getDisplayName());
        address.setZipCode(marketDTO.getZipCode());
        //
        addressService.saveAddress(address);
        User user = userService.getUserEntityById(marketDTO.getUserId());
        market.setUser(user);
        market.setAddress(address);
        return saveAndMap(market, "save");
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
        Address address = market.getAddress();

        City city = cityService.getCityEntityByName(request.getCityName());
        Region region = regionService.getRegionEntityByName(request.getRegionName(),city.getName());
        //Address doldurma
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());
        address.setRegion(region);
        address.setName(request.getDisplayName());
        address.setZipCode(request.getZipCode());

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

   /** @Override
    public void assignProduct(Long marketId, Long productId) {

        Market market = getMarketEntityById(marketId);
        Product product = productService.getProductEntityById(productId);
        market.getProducts().add(product);
        market.setStatus(Status.APPROVED);

        log.info("{} assigned product", GeneralUtil.extractUsername());
        marketRepository.save(market);
    }
    **/

    @Override
    public void deleteMarket(Long id) {
        Market market = getMarketEntityById(id);
        market.setIsActive(false);
        log.info("{} Deleted market: {}", GeneralUtil.extractUsername(), market);
        marketRepository.save(market);
    }

    @Override
    public Page<MarketResponse> getMarketsByStatusPageable(Status status, Pageable pageable) {
        return marketRepository.getMarketsByStatusAndIsActiveTrue(status, pageable)
                .map(marketMapper::mapToDto);
    }

    @Override
    public List<MarketResponse> getMarketsByStatus(Status status) {
        return marketRepository.getMarketsByStatusAndIsActiveTrue(status)
                .stream().map(marketMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketResponse> getMarketsByUserId(Long userId) {
        return marketRepository.findAllByUserIdAndIsActiveTrue(userId)
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
