package com.project.donate.repository;

import com.project.donate.dto.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductElasticSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    List<ProductDocument> findByNameContainingOrDescriptionContaining(String name, String description);
}
