package com.project.donate.model;

import com.project.donate.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartProduct {

    @EmbeddedId
    private CartProductId id;

    @ManyToOne
    @JoinColumn(name = "cart_id",  insertable = false, updatable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id",  insertable = false, updatable = false)
    private Product product;

    @Column(nullable = false)
    private Integer productQuantity;

    private Double productPrice;

    private LocalDateTime productAddedTime;

    @Enumerated(EnumType.STRING)
    private Status status;
}

