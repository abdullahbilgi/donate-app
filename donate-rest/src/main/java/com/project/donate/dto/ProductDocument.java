package com.project.donate.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.project.donate.Config.LocalDateTimeFromEpochMillisDeserializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "products")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

        private Double price;
        private Double discountedPrice;
        private Integer discount;
        private Integer quantity;
        private String imageUrl;
        private Boolean isActive;

        @JsonDeserialize(using = LocalDateTimeFromEpochMillisDeserializer.class)
        private LocalDateTime productionDate;
        @JsonDeserialize(using = LocalDateTimeFromEpochMillisDeserializer.class)
        private LocalDateTime expiryDate;
        @JsonDeserialize(using = LocalDateTimeFromEpochMillisDeserializer.class)
        private LocalDateTime lastDonatedDate;
        @JsonDeserialize(using = LocalDateTimeFromEpochMillisDeserializer.class)
        private LocalDateTime createdDate;
        @JsonDeserialize(using = LocalDateTimeFromEpochMillisDeserializer.class)
        private LocalDateTime lastModifiedDate;
      //  private Long categoryId;
       // private String categoryName;
        private String marketName;

        @Field(type = FieldType.Keyword)
        private Long cityId;
        private Long regionId;

}
