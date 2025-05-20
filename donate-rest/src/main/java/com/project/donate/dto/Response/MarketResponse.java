package com.project.donate.dto.Response;

import com.project.donate.model.Address;
import com.project.donate.model.Product;
import com.project.donate.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class MarketResponse {

    private Long id;

    private String name;

    private String taxNumber;

    private String status;

    private UserResponse user;

    private AddressResponse address;

   // private Set<ProductResponse> products;

}
