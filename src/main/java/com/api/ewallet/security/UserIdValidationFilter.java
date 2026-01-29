package com.api.ewallet.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class UserIdValidationFilter extends OncePerRequestFilter {

    private static final String USER_ID_HEADER = "X-User-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String userId = request.getHeader(USER_ID_HEADER);


        if (request.getRequestURI().startsWith("/v1/api/")) {
            log.debug("Validating request: {} {} - X-User-Id: {}", request.getMethod(), request.getRequestURI(), userId);

            if (StringUtils.isBlank(userId)) {
                log.warn("Missing X-User-Id header for request: {} {}", request.getMethod(), request.getRequestURI());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"errorDetails\":[{\"code\":\"EWLBE401\",\"message\":\"Missing X-User-Id header\"}]}");
                return;
            }

        }
        filterChain.doFilter(request, response);
    }
}
