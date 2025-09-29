package com.example.jobtracker.health;

import com.example.jobtracker.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<ResponseDto<String>> healthCheck() {
        ResponseDto<String> response = new ResponseDto<>(
                "Application is running successfully",
                "OK",
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detailed")
    public ResponseEntity<ResponseDto<Object>> detailedHealthCheck() {
        HealthStatus status = new HealthStatus(
                "UP",
                LocalDateTime.now(),
                "Job Application Tracker and Blog Platform",
                "1.0.0"
        );

        ResponseDto<Object> response = new ResponseDto<>(
                "Detailed health check",
                status,
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    public static class HealthStatus {
        private String status;
        private LocalDateTime timestamp;
        private String applicationName;
        private String version;

        public HealthStatus(String status, LocalDateTime timestamp, String applicationName, String version) {
            this.status = status;
            this.timestamp = timestamp;
            this.applicationName = applicationName;
            this.version = version;
        }

        // Getters
        public String getStatus() { return status; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getApplicationName() { return applicationName; }
        public String getVersion() { return version; }
    }
}
