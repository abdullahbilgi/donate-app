package com.project.donate.service;

import com.project.donate.dto.LogDTO;
import com.project.donate.model.Log;
import com.project.donate.repository.LogRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private static final File LOG_FILE = new File("logs/donate-app/donate.log");
    private long lastReadLine = 0;

    // regex pattern for your log line
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+(\\w+)\\s+\\[(.+?)]\\s+(\\S+)\\s+:\\s+(.*)$"
    );

    // Periodically check for new log lines
    @Scheduled(fixedDelay = 2000)
    public void readNewLogs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            int currentLine = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (currentLine++ < lastReadLine) continue;

                Matcher matcher = LOG_PATTERN.matcher(line);
                if (matcher.matches()) {
                    Log log = Log.builder()
                            .dateTime(LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .status(matcher.group(2))
                            .function(matcher.group(3))
                            .service(matcher.group(4))
                            .message(matcher.group(5))
                            .build();

                    logRepository.save(log);
                }
                lastReadLine++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LogDTO> getLatestLogs(int limit) {
        return logRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getDateTime().compareTo(a.getDateTime()))
                .limit(limit)
                .map(log -> LogDTO.builder()
                        .id(log.getId())
                        .dateTime(log.getDateTime())
                        .status(log.getStatus())
                        .function(log.getFunction())
                        .service(log.getService())
                        .message(log.getMessage())
                        .build())
                .toList();
    }

}
