package com.project.donate.service;

import com.project.donate.dto.LogDTO;

import java.util.List;

public interface LogService {
    List<LogDTO> getLatestLogs(int limit);
}
