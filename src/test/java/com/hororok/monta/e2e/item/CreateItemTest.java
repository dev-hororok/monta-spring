package com.hororok.monta.e2e.item;

import com.hororok.monta.dto.request.item.CreateItemRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.item.CreateItemResponseDto;
import com.hororok.monta.setting.TestSetting;
import com.hororok.monta.utils.ItemUtils;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class CreateItemTest extends ItemUtils {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, CreateItemRequestDto requestDto) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().post("/v2/admin/items")
                .then().log().all().extract();
    }

    @Test
    @DisplayName("성공")
    public void createItemByAdmin() {
        CreateItemRequestDto requestDto = createItemRequestDto(true);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        CreateItemResponseDto response = extractableResponse.as(CreateItemResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(201);
        assertThat(response.getStatus()).isEqualTo("success");

        deleteTestData(response.getData().getItemId());
    }

    @Test
    @DisplayName("실패 : 권한 없음")
    public void createItemByUser() {
        CreateItemRequestDto requestDto = createItemRequestDto(true);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");
    }

    @Test
    @DisplayName("실패 : 인증되지 않은 사용자")
    public void createItemByElse() {
        CreateItemRequestDto requestDto = createItemRequestDto(true);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }

    @Test
    @DisplayName("실패 : 유효성 에러")
    public void createItemByBlank() {
        CreateItemRequestDto requestDto = createItemRequestDto(false);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(400);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("name : 아이템 이름은 필수 입력 값 입니다.");
    }
}
