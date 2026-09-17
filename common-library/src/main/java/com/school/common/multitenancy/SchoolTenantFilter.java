package com.school.common.multitenancy;

import java.io.IOException;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SchoolTenantFilter extends OncePerRequestFilter {

    private static final Pattern VALID_SCHEMA_NAME = Pattern.compile("[A-Za-z0-9_]+");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
            //for all these requests we don't need to check for the tenant header and the default schema will be used that is school_management 
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || request.getRequestURI().contains("v3/api-docs")
                || request.getRequestURI().contains("swagger") || request.getRequestURI().contains("/schools/public/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String schoolName = request.getHeader(TenantContext.HEADER_NAME);
        if (schoolName == null || schoolName.isBlank()
                || !VALID_SCHEMA_NAME.matcher(schoolName).matches()) {
            response.sendError(
                    HttpStatus.BAD_REQUEST.value(),
                    "A valid X-School-Name header is required");
            return;
        }

        TenantContext.setTenant(schoolName);
        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
