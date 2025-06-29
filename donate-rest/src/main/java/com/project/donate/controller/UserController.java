package com.project.donate.controller;

import com.project.donate.dto.Response.UserResponse;
import com.project.donate.mapper.UserMapper;
import com.project.donate.model.User;
import com.project.donate.service.UserService;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        User user = userService.getUserEntityByUsername(GeneralUtil.extractUsername());
        UserResponse userResponse = userMapper.userToUserDto(user);
        return ResponseEntity.ok(userResponse);
    }
}
