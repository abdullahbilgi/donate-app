package com.project.donate.dto.Request;

import com.project.donate.records.ProductItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrganizationRequest {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 60, message = "Name must be between 1 and 60 characters")
    private String name;

    @NotBlank(message = "TaxNumber is mandatory")
    @Size(min = 5, max = 20, message = "TaxNumber must be between 5 and 20 characters")
    private String taxNumber;

    private String status;

    private Long userId;

    private AddressRequest address;

    private List<ProductItem> productItems;
}
