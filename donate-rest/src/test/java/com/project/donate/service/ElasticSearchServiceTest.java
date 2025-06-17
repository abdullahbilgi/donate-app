package com.project.donate.service;

import com.project.donate.dto.ProductDocument;
import com.project.donate.dto.Request.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ElasticSearchServiceTest {

    private ElasticsearchOperations elasticsearchOperations;
    private ElasticSearchService elasticSearchService;

    @BeforeEach
    void setUp() {
        elasticsearchOperations = mock(ElasticsearchOperations.class);
        elasticSearchService = new ElasticSearchService(elasticsearchOperations);
    }

    @Test
    void testSave() {
        ProductDocument doc = new ProductDocument();
        doc.setId(1L);
        doc.setName("Test Product");

        elasticSearchService.save(doc);

        verify(elasticsearchOperations, times(1))
                .save(doc, IndexCoordinates.of("products"));
    }

    @Test
    void testDelete() {
        Long productId = 1L;

        elasticSearchService.delete(productId);

        verify(elasticsearchOperations, times(1))
                .delete(productId, IndexCoordinates.of("products"));
    }

    @Test
    void testUpdate_whenProductExists_thenUpdateAndSave() {
        Long productId = 1L;

        ProductDocument existing = new ProductDocument();
        existing.setId(productId);
        existing.setName("Old Name");

        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setName("New Name");
        updateRequest.setProductionDate(LocalDate.parse("2024-02-01").atStartOfDay());
        updateRequest.setExpiryDate(LocalDate.parse("2025-01-01").atStartOfDay());
        updateRequest.setLastDonatedDate(LocalDate.parse("2024-06-01").atStartOfDay());
        updateRequest.setPrice(100.0);
        updateRequest.setDiscountedPrice(80.0);
        updateRequest.setQuantity(10);
        updateRequest.setDescription("Updated description");

        when(elasticsearchOperations.get(String.valueOf(productId), ProductDocument.class)).thenReturn(existing);

        elasticSearchService.update(productId, updateRequest);

        ArgumentCaptor<ProductDocument> captor = ArgumentCaptor.forClass(ProductDocument.class);
        verify(elasticsearchOperations).save(captor.capture());

        ProductDocument updated = captor.getValue();
        assertEquals("New Name", updated.getName());
        assertEquals(LocalDate.parse("2024-02-01").atStartOfDay(), updated.getProductionDate());
        assertEquals(LocalDate.parse("2025-01-01").atStartOfDay(), updated.getExpiryDate());
        assertEquals(LocalDate.parse("2024-06-01").atStartOfDay(), updated.getLastDonatedDate());
        assertEquals(100.0, updated.getPrice());
        assertEquals(80.0, updated.getDiscountedPrice());
        assertEquals(10, updated.getQuantity());
        assertEquals("Updated description", updated.getDescription());
    }

    @Test
    void testUpdate_whenProductDoesNotExist_thenDoNothing() {
        Long productId = 1L;
        ProductRequest updateRequest = new ProductRequest();

        when(elasticsearchOperations.get(String.valueOf(productId), ProductDocument.class)).thenReturn(null);

        elasticSearchService.update(productId, updateRequest);

        verify(elasticsearchOperations, never()).save(Optional.ofNullable(any()));
    }
}
