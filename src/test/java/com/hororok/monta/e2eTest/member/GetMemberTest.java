package com.hororok.monta.e2eTest.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.jwt.Setting;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class GetMemberTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Admin 권한 : 멤버 전체 조회에 성공")
    @Test
    public void getMembersbyAdmin() throws Exception {
        //given
        String token = Setting.adminAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/admin/members";

        //when
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("User 권한 : 멤버 전체 조회에 실패")
    @Test
    public void getMembersbyUser() throws Exception {
        //given
        String token = Setting.userAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/admin/members";

        //when
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        FailResponseDto responseDto = objectMapper.readValue(response.getBody(), FailResponseDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(responseDto.getMessage()).contains("해당 권한이 없습니다.");
    }

    @DisplayName("유효하지 않은 사용자 : 멤버 전체 조회에 실패")
    @Test
    public void getMembersbyNotUser() throws Exception {
        //given
        String token = Setting.notUserAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/admin/members";

        //when
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        FailResponseDto responseDto = objectMapper.readValue(response.getBody(), FailResponseDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseDto.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }
}

