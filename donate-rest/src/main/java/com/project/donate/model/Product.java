package com.project.donate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.donate.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    @Column(updatable = false)
    private LocalDateTime productionDate;

    @Column(updatable = false)
    private LocalDateTime expiryDate;

    private Double price;

    private Double discountedPrice;

    private Integer discount;

    private Integer quantity;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Builder.Default
    private Boolean isActive = true;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "market_id", nullable = false)
    private Market market;
}
