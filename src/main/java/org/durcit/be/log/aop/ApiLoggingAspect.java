package org.durcit.be.log.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.durcit.be.log.domain.Log;
import org.durcit.be.log.repository.LogRepository;
import org.durcit.be.security.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.util.Enumeration;

@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

    private final LogRepository logRepository;

    @Around("execution(* org.durcit.be..*Controller.*(..)) && !execution(* org.durcit.be.post.controller.EmojiController.*(..))")
    public Object logApiRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String memberId = SecurityUtil.getCurrentMemberIdOrNull();
        String endpoint = request.getRequestURI();
        String method = request.getMethod();
        String controller = joinPoint.getTarget().getClass().getSimpleName();
        String requestPayload = extractRequestBody(request);

        Object response;
        int statusCode = 200;
        String responsePayload = null;

        try {
            response = joinPoint.proceed();
            if (response instanceof ResponseEntity<?>) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>) response;
                statusCode = responseEntity.getStatusCode().value();
                responsePayload = serializeResponse(responseEntity);
            }
        } catch (Throwable e) {
            statusCode = 500;
            responsePayload = e.getMessage();
            throw e;
        }

        Log log = Log.builder()
                .memberId(memberId)
                .method(method)
                .endpoint(endpoint)
                .controller(controller)
                .requestPayload(requestPayload)
                .responsePayload(responsePayload)
                .statusCode(statusCode)
                .build();

        logRepository.save(log);

        return response;
    }

    private String serializeResponse(ResponseEntity<?> response) {
        return response.getBody() == null ? "" : response.getBody().toString();
    }

    private String extractRequestBody(HttpServletRequest request) {
        StringBuilder body = new StringBuilder();

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String replaceParam = parameterName.replace("\\.", "-");
            String[] values = request.getParameterValues(parameterName);
            if (values != null) {
                for (String value : values) {
                    body.append(replaceParam).append("=").append(value).append("&");
                }
            }
        }

        if (!body.isEmpty() && body.charAt(body.length() - 1) == '&') {
            body.setLength(body.length() - 1);
        }

        if (request.getContentType() != null && request.getContentType().contains("json")) {
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            } catch (Exception e) {
                body.append("Error parsing JSON body");
            }
        }

        return body.toString();
    }




}
