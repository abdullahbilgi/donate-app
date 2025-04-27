package com.project.donate.Config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(Map.of(
                "cloud_name", "dxa6obkhc",
                "api_key", "628987816848172",
                "api_secret", "rHHpyoRpUdxxYE_qnFrlwd9rMsI"
        ));
    }
}
