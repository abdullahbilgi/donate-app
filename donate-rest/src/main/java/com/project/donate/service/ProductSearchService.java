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
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


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
        Long cityId = city.getId();

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

        Query regionQuery = TermQuery.of(t -> t
                .field("regionId")
                .value(regionId.toString())
        )._toQuery();

        Query cityQuery = TermQuery.of(t -> t
                .field("cityId")
                .value(cityId.toString())
        )._toQuery();

        // isActive == true filtresi
        Query isActiveQuery = TermQuery.of(t -> t
                .field("isActive")
                .value("true")
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
                                                    .filter(isActiveQuery) // sadece aktif ürünler
                                            )
                                    )
                                    .functions(fns -> fns.filter(regionQuery).weight(10.0))
                                    .functions(fns -> fns.filter(cityQuery).weight(5.0))
                                    .boostMode(FunctionBoostMode.Multiply)
                            )
                    )
            );

            SearchResponse<ProductDocument> response = elasticsearchClient.search(request, ProductDocument.class);
            List<ProductDocument> products = response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
            long totalHits = response.hits().total() != null ? response.hits().total().value() : 0;

            return new PageImpl<>(products, pageable, totalHits);

        } catch (IOException e) {
            log.error("Elasticsearch search failed: {}", e.getMessage(), e);
            throw new RuntimeException("Elasticsearch sorgusu başarısız oldu", e);
        }
    }

    public Page<ProductDocument> getAllProductsPrioritizedByLocation(Pageable pageable) {
        User user = userService.getUserEntityByUsername(GeneralUtil.extractUsername());
        Long regionId = user.getAddress().getRegion().getId();
        City city = regionService.getRegionsCityEntityById(regionId);
        Long cityId = city.getId();

        Query regionQuery = TermQuery.of(t -> t
                .field("regionId")
                .value(v -> v.longValue(regionId))   // numeric
        )._toQuery();

        Query cityQuery = TermQuery.of(t -> t
                .field("cityId")
                .value(v -> v.longValue(cityId))     // numeric
        )._toQuery();

        Query isActiveQuery = TermQuery.of(t -> t
                .field("isActive")
                .value(v -> v.booleanValue(true))    // boolean
        )._toQuery();

        try {
            // 1) Builder'ı kur
            SearchRequest.Builder b = new SearchRequest.Builder()
                    .index("products")
                    .from((int) pageable.getOffset())
                    .size(pageable.getPageSize())
                    .query(q -> q.functionScore(fs -> fs
                            .query(inner -> inner.bool(bq -> bq.must(isActiveQuery)))
                            .functions(fns -> fns.filter(regionQuery).weight(10.0))
                            .functions(fns -> fns.filter(cityQuery).weight(5.0))
                            .boostMode(FunctionBoostMode.Multiply)
                    ));

            // 2) Sıralama: Pageable -> ES sort
            if (pageable.getSort().isSorted()) {
                pageable.getSort().forEach(o -> b.sort(so -> so.field(f -> f
                        .field(esSortField(o.getProperty()))
                        .order(o.isAscending() ? SortOrder.Asc : SortOrder.Desc)
                        .missing("_last") // alan yoksa en sona
                )));
                // Tie-breaker istersen:
                b.sort(so -> so.field(f -> f.field("id").order(SortOrder.Asc)));
            } else {
                // Varsayılan: skor (mevcut önceliklendirme)
                // b.sort(so -> so.score(sc -> sc.order(SortOrder.Desc)));
            }

            // 3) Build et
            SearchRequest request = b.build();

            SearchResponse<ProductDocument> response =
                    elasticsearchClient.search(request, ProductDocument.class);

            var products = response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

            long totalHits = response.hits().total() != null ? response.hits().total().value() : 0;
            return new PageImpl<>(products, pageable, totalHits);

        } catch (IOException e) {
            log.error("Elasticsearch search failed: {}", e.getMessage(), e);
            throw new RuntimeException("Elasticsearch sorgusu başarısız oldu", e);
        }
    }

    // Text field'ları sıralarken keyword kullan
    private String esSortField(String prop) {
        return switch (prop) {
            case "name" -> "name.keyword";
            case "price" -> "price";
            case "discountedPrice" -> "discountedPrice";
            case "createdDate" -> "createdDate";
            default -> prop;
        };
    }


    public Page<ProductDocument> getAllDonatedProductsPrioritizedByLocation(Pageable pageable) {
        User user = userService.getUserEntityByUsername(GeneralUtil.extractUsername());
        Long regionId = user.getAddress().getRegion().getId();
        City city = regionService.getRegionsCityEntityById(regionId);
        Long cityId = city.getId();

        // Elasticsearch sorguları
        Query regionQuery = TermQuery.of(t -> t
                .field("regionId")
                .value(regionId.toString())
        )._toQuery();

        Query cityQuery = TermQuery.of(t -> t
                .field("cityId")
                .value(cityId.toString())
        )._toQuery();

        Query isActiveQuery = TermQuery.of(t -> t
                .field("isActive")
                .value("true")
        )._toQuery();
        /**

        Query productStatusQuery = TermQuery.of(t -> t
                .field("productStatus")
                .value("DONATE")
        )._toQuery();
         **/

        Query priceZeroQuery = TermQuery.of(t -> t
                .field("price")
                .value("0.0")
        )._toQuery();

        Query discountedPriceZeroQuery = TermQuery.of(t -> t
                .field("discountedPrice")
                .value("0.0")
        )._toQuery();

        // price == 0.0 OR discountedPrice == 0.0
        Query priceConditionQuery = BoolQuery.of(b -> b
                .should(priceZeroQuery)
                .should(discountedPriceZeroQuery)
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
                                                    .must(isActiveQuery)
                                                    //.must(productStatusQuery)
                                                    .must(priceConditionQuery)
                                            )
                                    )
                                    .functions(fns -> fns.filter(regionQuery).weight(10.0))
                                    .functions(fns -> fns.filter(cityQuery).weight(5.0))
                                    .boostMode(FunctionBoostMode.Multiply)
                            )
                    )
            );

            SearchResponse<ProductDocument> response = elasticsearchClient.search(request, ProductDocument.class);
            List<ProductDocument> products = response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
            long totalHits = response.hits().total() != null ? response.hits().total().value() : 0;

            return new PageImpl<>(products, pageable, totalHits);

        } catch (IOException e) {
            log.error("Elasticsearch search failed: {}", e.getMessage(), e);
            throw new RuntimeException("Elasticsearch sorgusu başarısız oldu", e);
        }
    }

}
