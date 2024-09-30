package com.spring.demo.core.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Pattern;

public class UrlFilter implements Filter {

    private static final Pattern INVALID_URL_PATTERN = Pattern.compile("[^\\w?&=:/]|(?<!:)/{2,}");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        // 쿼리의 !! 도 검사하기 위해 추가함
        String queryString = httpRequest.getQueryString();

        // /h2-console 경로는 필터 적용에서 제외
        if (requestURI.startsWith("/h2-console")) {
            chain.doFilter(request, response);
            return;
        }

        // URI 검사
        if (INVALID_URL_PATTERN.matcher(requestURI).find()) {
            sendResponse(httpResponse, "유효하지 않은 문자가 주소에 포함되어 있습니다.");
            return;
        }

        // 쿼리 문자열 검사
        if (queryString != null && INVALID_URL_PATTERN.matcher(queryString).find()) {
            sendResponse(httpResponse, "유효하지 않은 문자가 주소에 포함되어 있습니다.");
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"reason\": \"" + message + "\"}");
    }
}
