package com.hororok.monta.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hororok.monta.dto.response.FailResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401 에러 반환
        FailResponseDto failResponseDto = new FailResponseDto();
        failResponseDto.setMessage("접근 불가");
        List<String> errors = new ArrayList<>();
        errors.add("인증되지 않은 사용자의 접근입니다.");
        failResponseDto.setErrors(errors);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper.writeValueAsString(failResponseDto);
        response.getWriter().write(jsonResponse);
    }
}