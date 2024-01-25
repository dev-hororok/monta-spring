package com.hororok.monta.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hororok.monta.dto.response.FailResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        //필요한 권한이 없이 접근하려 할때 403 에러 반환

        FailResponseDto failResponseDto = new FailResponseDto();
        failResponseDto.setMessage("권한 없음");
        List<String> errors = new ArrayList<>();
        errors.add("해당 권한이 없습니다.");
        failResponseDto.setErrors(errors);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper.writeValueAsString(failResponseDto);
        response.getWriter().write(jsonResponse);
    }
}