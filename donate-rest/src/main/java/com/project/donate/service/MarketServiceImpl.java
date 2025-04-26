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
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
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
        log.info("{} - looked market with id: {}", GeneralUtil.extractUsername(), id);
        return marketRepository.findById(id)
                .map(marketMapper::map)
                .orElseThrow(() -> {
                    log.error("{} market not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Market not found: " + id);
                });
    }


    @Override
    public MarketDTO createMarket(MarketDTO marketDTO) {
        Market market = marketMapper.mapDto(marketDTO);
        return saveAndMap(market, "save");
    }

    @Override
    public MarketDTO updateMarket(Long id, MarketDTO marketDTO) {
        MarketDTO dto = getMarketById(id);
        Market savingMarket = marketMapper.mapDto(marketDTO);
        savingMarket.setId(id);
        savingMarket.setStatus(Status.valueOf(dto.getStatus()));
        savingMarket.setIsActive(dto.getIsActive());
        return saveAndMap(savingMarket, "update");
    }

    @Override
    public void enabledMarket(Long id) {
        MarketDTO marketDTO = getMarketById(id);
        Market market = marketMapper.mapDto(marketDTO);
        market.setStatus(Status.APPROVED);
        market.setIsActive(true);

        log.info("{} enabled market", GeneralUtil.extractUsername());
        marketRepository.save(market);

    }

    @Override
    public void assignProduct(Long marketId, Long productId) {

        Market market = marketMapper.mapDto(getMarketById(marketId));
        Product product = productMapper.mapDto(productService.getProductById(productId));
        market.getProducts().add(product);
        market.setIsActive(true);
        market.setStatus(Status.APPROVED);

        log.info("{} assigned product", GeneralUtil.extractUsername());
        marketRepository.save(market);

    }

    @Override
    public void deleteMarket(Long id) {
        MarketDTO marketDTO = getMarketById(id);
        Market market = marketMapper.mapDto(marketDTO);
        market.setIsActive(false);
        saveAndMap(market, "delete");

    }

    private MarketDTO saveAndMap(Market market, String status) {
        Market savedMarket = marketRepository.save(market);

        switch (status) {
            case "save" -> log.info("{} Created market: {}", GeneralUtil.extractUsername(), market);
            case "update" -> log.info("{} Updated market: {}", GeneralUtil.extractUsername(), market);
            case "delete" -> log.info("{} Deleted market: {}", GeneralUtil.extractUsername(), market);
        }

        return marketMapper.map(savedMarket);
    }
}
