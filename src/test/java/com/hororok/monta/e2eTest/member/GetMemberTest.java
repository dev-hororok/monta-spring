package com.hororok.monta.e2eTest.member;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.member.GetMembersResponseDto;
import com.hororok.monta.setting.TestSetting;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class GetMemberTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .when().get("/admin/members")
                .then().log().all().extract();
    }

    @Transactional
    @Test
    @DisplayName("성공")
    public void getMembersByAdmin() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin");
        GetMembersResponseDto response = extractableResponse.as(GetMembersResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");
    }

    @Transactional
    @Test
    @DisplayName("실패 : 권한 없음")
    public void getMembersByUser() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User");
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");
    }

    @Transactional
    @Test
    @DisplayName("실패 : 인증되지 않은 사용자")
    public void getMembersByElse() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else");
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }
}

