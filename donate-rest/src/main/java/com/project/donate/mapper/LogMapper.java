package com.project.donate.mapper;

import com.project.donate.dto.LogDTO;
import com.project.donate.model.Log;
import org.springframework.stereotype.Component;

@Component
public class LogMapper implements ObjectMapper<Log, LogDTO> {

    @Override
    public LogDTO map(Log Log) {
        return LogDTO.builder()
                .id(Log.getId())
                .status(Log.getStatus())
                .dateTime(Log.getDateTime())
                .function(Log.getFunction())
                .service(Log.getService())
                .message(Log.getMessage())
                .build();
    }

    @Override
    public Log mapDto(LogDTO LogDTO) {
        return Log.builder()
                .id(LogDTO.getId())
                .status(LogDTO.getStatus())
                .dateTime(LogDTO.getDateTime())
                .function(LogDTO.getFunction())
                .service(LogDTO.getService())
                .message(LogDTO.getMessage())
                .build();
    }
}