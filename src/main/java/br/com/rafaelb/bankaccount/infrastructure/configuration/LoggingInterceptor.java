package br.com.rafaelb.bankaccount.infrastructure.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";
    private static final String REQUEST_ID = "requestId";

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);

        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(REQUEST_ID, requestId);

        log.info("""
                ===================== HTTP REQUEST =====================
                RequestId  : {}
                Method     : {}
                DateTime   : {}
                URI        : {}
                Query      : {}
                Client IP  : {}
                User-Agent : {}
                ========================================================
                """,
                requestId,
                request.getMethod(),
                formatDateTime(LocalDateTime.now()),
                request.getRequestURI(),
                request.getQueryString() != null ? request.getQueryString() : "-",
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
        );

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        long startTime = (Long) request.getAttribute(START_TIME);
        long elapsed = System.currentTimeMillis() - startTime;
        try {
            if (response.getStatus() >= 400) {
                log.error("""
                        ====================== HTTP ERROR =====================
                        RequestId : {}
                        Method    : {}
                        URI       : {}
                        Status    : {}
                        Duration  : {} ms
                        DateTime  : {}
                        ========================================================
                        """,
                        MDC.get(REQUEST_ID),
                        request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        elapsed,
                        formatDateTime(LocalDateTime.now()),
                        ex
                );
            } else {
                log.info("""
                        ===================== HTTP RESPONSE ====================
                        RequestId : {}
                        Method    : {}
                        URI       : {}
                        Status    : {}
                        Duration  : {} ms
                        ========================================================
                        """,
                        MDC.get(REQUEST_ID),
                        request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        elapsed
                );
            }
        } finally {
            MDC.clear();
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}