package com.school.common.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class BaseService {

    public String getSchoolCodeFromRequestHeader() {
        HttpServletRequest request = getCurrentRequest();
        return request.getHeader("X-School-Name");
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes == null) {
            throw new IllegalStateException("No current request bound to this thread.");
        }
        
        return attributes.getRequest();
    }
}