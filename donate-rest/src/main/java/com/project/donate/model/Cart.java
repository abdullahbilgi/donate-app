package com.project.donate.model;

import com.project.donate.records.ProductItem;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import com.project.donate.enums.Status;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "jsonb",nullable = false)
    @Type(JsonType.class)
    private List<ProductItem> productItems;

    @Column(nullable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(updatable = false)
    private LocalDateTime purchaseDate;

    @Column(updatable = false)
    private LocalDateTime expiredDate;

    private Boolean isActive;

    @PrePersist
    public void prePersist() {
        this.purchaseDate = LocalDateTime.now();
        this.expiredDate = this.purchaseDate.plusDays(1);
    }
}
