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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;
    private final MarketMapper marketMapper;
    private final ProductService productService;
    private final ProductMapper productMapper;


    @Override
    public List<MarketDTO> getAllMarket() {
        return marketRepository.findAll()
                .stream().map(marketMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public MarketDTO getMarketById(Long id) {
        return marketRepository.findById(id)
                .map(marketMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Market not found: " + id));
    }


    @Override
    public MarketDTO createMarket(MarketDTO marketDTO) {
        Market market = marketMapper.mapDto(marketDTO);
        return saveAndMap(market);
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
        MarketDTO marketDTO = getMarketById(id);
        Market market = marketMapper.mapDto(marketDTO);
        market.setStatus(Status.APPROVED);
        market.setIsActive(true);
        marketRepository.save(market);

    }

    @Override
    public void assignProduct(Long marketId, Long productId) {

        Market market = marketMapper.mapDto(getMarketById(marketId));
        Product product = productMapper.mapDto(productService.getProductById(productId));
        market.getProducts().add(product);
        market.setIsActive(true);
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

    private MarketDTO saveAndMap(Market market) {
        Market savedMarket = marketRepository.save(market);

        return marketMapper.map(savedMarket);
    }
}
