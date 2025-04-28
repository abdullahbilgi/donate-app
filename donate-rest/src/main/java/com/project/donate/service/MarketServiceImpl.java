package com.project.donate.service;

import com.project.donate.dto.MarketDTO;
import com.project.donate.dto.ProductDTO;
import com.project.donate.enums.Status;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.MarketMapper;
import com.project.donate.mapper.ProductMapper;
import com.project.donate.model.Market;
import com.project.donate.model.Product;
import com.project.donate.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;
    private final MarketMapper marketMapper;
    private final ProductService productService;


    @Override
    public List<MarketDTO> getAllMarket() {
        return marketRepository.findByIsActiveTrue()
                .stream().map(marketMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public MarketDTO getMarketById(Long id) {
        return marketMapper.map(getMarketEntityById(id));
    }


    @Override
    public MarketDTO createMarket(MarketDTO marketDTO) {
        Market market = marketMapper.mapDto(marketDTO);
        return saveAndMap(market);
    }

    @Override
    public Market getMarketEntityById(Long id) {
        return marketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Market not found: " + id));
    }

    @Override
    public MarketDTO updateMarket(Long id, MarketDTO marketDTO) {
        MarketDTO dto = getMarketById(id);
        Market savingMarket = marketMapper.mapDto(marketDTO);
        savingMarket.setId(id);
        savingMarket.setStatus(Status.valueOf(dto.getStatus()));
        savingMarket.setIsActive(dto.getIsActive());
        return saveAndMap(savingMarket);
    }

    @Override
    public void enabledMarket(Long id) {
        Market market = getMarketEntityById(id);
        market.setStatus(Status.APPROVED);
        marketRepository.save(market);
    }

    //TODO  reject market ve cancel market eklenecek

    @Override
    public void assignProduct(Long marketId, Long productId) {

        Market market = getMarketEntityById(marketId);
        Product product = productService.getProductEntityById(productId);
        market.getProducts().add(product);
        market.setStatus(Status.APPROVED);
        marketRepository.save(market);
    }

    @Override
    public void deleteMarket(Long id) {
        MarketDTO marketDTO = getMarketById(id);
        Market market = marketMapper.mapDto(marketDTO);
        market.setIsActive(false);
        saveAndMap(market);

    }

    @Override
    public Page<MarketDTO> getMarketsByStatusPageable(Status status, Pageable pageable) {
        return marketRepository.getMarketsByStatusAndIsActiveTrue(status,pageable)
                .map(marketMapper::map);
    }

    @Override
    public List<MarketDTO> getMarketsByStatus(Status status) {
        return marketRepository.getMarketsByStatusAndIsActiveTrue(status)
                .stream().map(marketMapper::map)
                .collect(Collectors.toList());
    }

    private MarketDTO saveAndMap(Market market) {
        Market savedMarket = marketRepository.save(market);

        return marketMapper.map(savedMarket);
    }
}
