package com.project.donate.controller;

import com.project.donate.dto.Request.ProductRequest;
import com.project.donate.dto.Request.UserDonateRequest;
import com.project.donate.dto.Response.ProductResponse;
import com.project.donate.dto.Response.UserDonateResponse;
import com.project.donate.service.UserDonateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/userDonate")
public class UserDonateController {

    private final UserDonateService userDonateService;

    @PostMapping
    public ResponseEntity<UserDonateResponse> createProduct(@RequestBody UserDonateRequest request) {
        return ResponseEntity.ok(userDonateService.createProduct(request));
    }
}
