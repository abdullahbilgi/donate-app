package com.project.donate.dto.Response;

import com.project.donate.records.ProductItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class OrganizationResponse {

    private Long id;

    private String name;

    private String taxNumber;

    private String status;

    private UserResponse user;

    private AddressResponse address;

    private List<ProductItem> productItems;
}
