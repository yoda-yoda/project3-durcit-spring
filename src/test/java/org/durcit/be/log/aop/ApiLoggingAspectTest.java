package org.durcit.be.log.aop;

import org.durcit.be.log.domain.Log;
import org.durcit.be.log.repository.LogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiLoggingAspectTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LogRepository logRepository;

    @Test
    @DisplayName("로깅 AOP 작동 테스트 - POST -> 성공")
    void test_api_logging_aspect_post() throws Exception {
        // given
        String requestBody = "{\"username\": \"testuser\", \"password\": \"123456\"}";

        // when
        mockMvc.perform(post("/api/test/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        // then
        Log savedLog = logRepository.findAll().get(0);
        assertThat(savedLog.getEndpoint()).isEqualTo("/api/test/login");
        assertThat(savedLog.getMethod()).isEqualTo("POST");
        assertThat(savedLog.getRequestPayload()).contains("\"username\": \"testuser\"");
        assertThat(savedLog.getResponsePayload()).isNotNull();
    }
    
    @Test
    @DisplayName("로깅 AOP 작동 테스트 - GET -> 성공")
    void test_api_logging_aspect_get() throws Exception {
        // when
        mockMvc.perform(get("/api/test/hello"))
                .andExpect(status().isOk());
        // then
        Log savedLog = logRepository.findAll().get(0);
        assertThat(savedLog.getEndpoint()).isEqualTo("/api/test/hello");
        assertThat(savedLog.getMethod()).isEqualTo("GET");
        assertThat(savedLog.getRequestPayload()).isEmpty();
        assertThat(savedLog.getResponsePayload()).contains("hello");
    }
    



}