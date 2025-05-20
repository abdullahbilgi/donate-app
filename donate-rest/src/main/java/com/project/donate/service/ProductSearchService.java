package com.project.donate.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.donate.dto.ProductDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductSearchService{

    private final ElasticsearchClient elasticsearchClient;

    public Page<ProductDocument> searchByTextAndCity(String keyword, Long cityId, Pageable pageable) {

        // Fuzzy match queries
        Query nameQuery = MatchQuery.of(m -> m
                .field("name")
                .query(keyword)
                .fuzziness("AUTO")
        )._toQuery();

        Query descriptionQuery = MatchQuery.of(m -> m
                .field("description")
                .query(keyword)
                .fuzziness("AUTO")
        )._toQuery();

        // Boost cityId match
        Query cityBoostQuery = TermQuery.of(t -> t
                .field("cityId")
                .value(cityId.toString())
        )._toQuery();

        try {
            SearchRequest request = SearchRequest.of(s -> s
                    .index("products")
                    .from((int) pageable.getOffset())
                    .size(pageable.getPageSize())
                    .query(q -> q
                            .functionScore(fs -> fs
                                    .query(inner -> inner
                                            .bool(b -> b
                                                    .should(nameQuery)
                                                    .should(descriptionQuery)
                                            )
                                    )
                                    .functions(fn -> fn
                                            .filter(cityBoostQuery)
                                            .weight(5.0)
                                    )
                                    .boostMode(FunctionBoostMode.Multiply)
                            )
                    )
            );

            SearchResponse<ProductDocument> response = elasticsearchClient.search(request, ProductDocument.class);

            List<ProductDocument> products = response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());

            long totalHits = response.hits().total() != null ? response.hits().total().value() : 0;

            return new PageImpl<>(products, pageable, totalHits);

        } catch (IOException e) {
            log.error("Elasticsearch search failed: {}", e.getMessage(), e);
            throw new RuntimeException("Elasticsearch sorgusu başarısız oldu", e);
        }
    }
}
