package com.project.donate.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class GeneralUtil {

    public static String extractUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
