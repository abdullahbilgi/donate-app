package com.project.donate.service;

import com.project.donate.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


public interface UserService {
    User getUserEntityById(Long id);
}
