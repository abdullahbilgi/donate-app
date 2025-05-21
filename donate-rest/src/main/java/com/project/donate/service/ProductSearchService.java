package com.project.donate.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.donate.dto.ProductDocument;
import com.project.donate.model.City;
import com.project.donate.model.User;
import com.project.donate.util.GeneralUtil;
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
    private final RegionService regionService;
    private final UserService userService;

    public Page<ProductDocument> searchByTextAndCity(String keyword, Pageable pageable) {

        User user = userService.getUserEntityByUsername(GeneralUtil.extractUsername());
        Long regionId = user.getAddress().getRegion().getId();

        City city = regionService.getRegionsCityEntityById(regionId);
        Long cityId = city.getId(); // cityId artık veritabanından geldi

        // Text bazlı fuzzy match'ler
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

        // Region ve City filtreleri
        Query regionQuery = TermQuery.of(t -> t
                .field("regionId")
                .value(regionId.toString())
        )._toQuery();

        Query cityQuery = TermQuery.of(t -> t
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
                                    .functions(fns -> fns
                                            .filter(regionQuery).weight(10.0)
                                    )
                                    .functions(fns -> fns
                                            .filter(cityQuery).weight(5.0)
                                    )
                                    .boostMode(FunctionBoostMode.Multiply)
                            )
                    )
            );

            SearchResponse<ProductDocument> response = elasticsearchClient.search(request, ProductDocument.class);

            List<ProductDocument> products = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            long totalHits = response.hits().total() != null ? response.hits().total().value() : 0;

            return new PageImpl<>(products, pageable, totalHits);

        } catch (IOException e) {
            log.error("Elasticsearch search failed: {}", e.getMessage(), e);
            throw new RuntimeException("Elasticsearch sorgusu başarısız oldu", e);
        }
    }


    public Page<ProductDocument> getAllProductsPrioritizedByLocation( Pageable pageable){
        User user = userService.getUserEntityByUsername(GeneralUtil.extractUsername());
        Long regionId = user.getAddress().getRegion().getId();
        City city = regionService.getRegionsCityEntityById(regionId);
        Long cityId = city.getId();

        Query regionQuery = TermQuery.of(t -> t
                .field("regionId")
                .value(regionId.toString())
        )._toQuery();

        Query cityQuery = TermQuery.of(t -> t
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
                                            .matchAll(m -> m) // Arama yok, tüm ürünler
                                    )
                                    .functions(fns -> fns
                                            .filter(regionQuery).weight(10.0)
                                    )
                                    .functions(fns -> fns
                                            .filter(cityQuery).weight(5.0)
                                    )
                                    .boostMode(FunctionBoostMode.Multiply)
                            )
                    )
            );

            SearchResponse<ProductDocument> response = elasticsearchClient.search(request, ProductDocument.class);

            List<ProductDocument> products = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            long totalHits = response.hits().total() != null ? response.hits().total().value() : 0;

            return new PageImpl<>(products, pageable, totalHits);

        } catch (IOException e) {
            log.error("Elasticsearch search failed: {}", e.getMessage(), e);
            throw new RuntimeException("Elasticsearch sorgusu başarısız oldu", e);
        }
    }
}
