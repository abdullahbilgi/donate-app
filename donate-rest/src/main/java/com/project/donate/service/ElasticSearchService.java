package com.project.donate.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.project.donate.dto.ProductDocument;
import com.project.donate.dto.Request.ProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2

public class ElasticSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public void save(ProductDocument document) {
        elasticsearchOperations.save(document, IndexCoordinates.of("products"));
    }

    public void delete(ProductDocument document) {
        try {
            if (document == null || document.getId() == null) {
                log.warn("Güncellenecek Elasticsearch dokümanı geçersiz.");
                return;
            }

            // Elasticsearch'te güncelleme yapılır
            elasticsearchOperations.save(document, IndexCoordinates.of("products"));

            log.info("Elasticsearch dokümanı güncellendi: id={}", document.getId());

        } catch (Exception e) {
            log.error("Elasticsearch güncelleme hatası: {}", e.getMessage(), e);
            throw new RuntimeException("Elasticsearch güncelleme başarısız", e);
        }
    }



    public void update(Long productId, ProductRequest updateRequest) {
        ProductDocument existing = elasticsearchOperations.get(String.valueOf(productId), ProductDocument.class);

        if (existing != null) {
            existing.setName(updateRequest.getName());
            existing.setProductionDate(updateRequest.getProductionDate());
            existing.setExpiryDate(updateRequest.getExpiryDate());
            existing.setLastDonatedDate(updateRequest.getLastDonatedDate());
            existing.setPrice(updateRequest.getPrice());
            existing.setDiscountedPrice(updateRequest.getDiscountedPrice());
            //existing.setProductStatus(updateRequest.getProductStatus());
            existing.setQuantity(updateRequest.getQuantity());
            existing.setDescription(updateRequest.getDescription());
            //existing.setCategoryId(updateRequest.getCategoryId());
            //existing.setMarketId(updateRequest.getMarketId());
            elasticsearchOperations.save(existing);
        }
    }
}
