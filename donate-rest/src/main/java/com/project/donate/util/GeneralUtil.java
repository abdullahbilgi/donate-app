package com.project.donate.util;

import com.project.donate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class GeneralUtil {

    public static String extractUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
