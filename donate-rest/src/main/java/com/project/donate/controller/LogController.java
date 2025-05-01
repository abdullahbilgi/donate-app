package com.project.donate.controller;

import com.project.donate.dto.LogDTO;
import com.project.donate.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs")
public class LogController {

    private final LogService logService;

    @GetMapping("/latest")
    public List<LogDTO> getLatestLogs(@RequestParam(defaultValue = "20") int limit) {
        return logService.getLatestLogs(limit);
    }

}
