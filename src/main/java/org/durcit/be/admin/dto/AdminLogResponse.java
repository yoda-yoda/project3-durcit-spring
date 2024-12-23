package org.durcit.be.admin.dto;

import lombok.Builder;
import lombok.Getter;
import org.durcit.be.log.domain.Log;

import java.time.LocalDateTime;

@Getter
public class AdminLogResponse {

    private Long id;
    private String memberId;
    private String method;
    private String endpoint;
    private String controller;
    private String requestPayload;
    private String responsePayload;
    private int statusCode;
    private LocalDateTime timestamp;

    @Builder
    public AdminLogResponse(Long id, String memberId, String method, String endpoint, String controller, String requestPayload, String responsePayload, int statusCode, LocalDateTime timestamp) {
        this.id = id;
        this.memberId = memberId;
        this.method = method;
        this.endpoint = endpoint;
        this.controller = controller;
        this.requestPayload = requestPayload;
        this.responsePayload = responsePayload;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

    public static AdminLogResponse fromEntity(Log log) {
        return AdminLogResponse.builder()
                .id(log.getId())
                .memberId(log.getMemberId())
                .method(log.getMethod())
                .endpoint(log.getEndpoint())
                .controller(log.getController())
                .requestPayload(log.getRequestPayload())
                .responsePayload(log.getResponsePayload())
                .statusCode(log.getStatusCode())
                .timestamp(log.getTimestamp())
                .build();
    }



}
