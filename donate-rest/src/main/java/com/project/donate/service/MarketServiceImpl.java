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
        MarketDTO marketDTO = getMarketById(id);
        Market market = marketMapper.mapDto(marketDTO);
        market.setIsActive(false);
        saveAndMap(market, "delete");

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
