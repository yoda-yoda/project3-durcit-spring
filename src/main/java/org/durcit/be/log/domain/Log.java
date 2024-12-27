package org.durcit.be.log.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "api_log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "api_id")
    private Long id;

    private String memberId;

    private String method;

    private String endpoint;

    private String controller;

    @Lob
    private String requestPayload;
    @Lob
    private String responsePayload;

    private int statusCode;

    private LocalDateTime timestamp;

    @Builder
    public Log(String memberId, String method, String endpoint, String controller, String requestPayload, String responsePayload, int statusCode) {
        this.memberId = memberId;
        this.method = method;
        this.endpoint = endpoint;
        this.controller = controller;
        this.requestPayload = requestPayload;
        this.responsePayload = responsePayload;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }
}
