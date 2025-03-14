package com.project.donate.model;

import com.project.donate.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
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

    private LocalDateTime productionDate;

    private LocalDateTime expiryDate;

    private Double price;

    private Double discountedPrice;

    private Integer discount;

    private Integer quantity;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus; //todo: fill it

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


}
