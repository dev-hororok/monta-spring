package com.hororok.monta.e2eTest.palette;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.jwt.Setting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class GetPaletteTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("성공")
    @Test
    public void getPalettesbyAdmin() throws Exception {
        //given
        HttpEntity<String> entity = Setting.returnEntity("Admin");
        String url = "http://localhost:" + port + "/admin/palettes";

        //when
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("실패 : 권한 없음")
    @Test
    public void getPalettesbyUser() throws Exception {
        //given
        HttpEntity<String> entity = Setting.returnEntity("User");
        String url = "http://localhost:" + port + "/admin/palettes";

        //when
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        FailResponseDto responseDto = objectMapper.readValue(response.getBody(), FailResponseDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(responseDto.getMessage()).contains("해당 권한이 없습니다.");
    }

    @DisplayName("실패 : 인증되지 않은 사용자")
    @Test
    public void getPalettesbyElse() throws Exception {
        //given
        HttpEntity<String> entity = Setting.returnEntity("Else");
        String url = "http://localhost:" + port + "/admin/palettes";

        //when
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        FailResponseDto responseDto = objectMapper.readValue(response.getBody(), FailResponseDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseDto.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }
}
