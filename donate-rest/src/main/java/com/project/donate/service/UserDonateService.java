package com.project.donate.service;

import com.project.donate.dto.Request.ProductRequest;
import com.project.donate.dto.Request.UserDonateRequest;
import com.project.donate.dto.Response.ProductResponse;
import com.project.donate.dto.Response.UserDonateResponse;

public interface UserDonateService {

    UserDonateResponse createProduct(UserDonateRequest request);
}
