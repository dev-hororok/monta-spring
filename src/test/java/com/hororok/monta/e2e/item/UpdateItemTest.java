package com.hororok.monta.e2e.item;

import com.hororok.monta.dto.request.item.UpdateItemRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.item.UpdateItemResponseDto;
import com.hororok.monta.entity.Item;
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
public class UpdateItemTest extends ItemUtils {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, UpdateItemRequestDto requestDto, int itemId) {
        String url = "/v2/admin/items/" + itemId;

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().patch(url)
                .then().log().all().extract();
    }

    @Test
    @DisplayName("성공")
    public void updateItemByAdmin() {
        Item item = saveItem(createItemRequestDto(true));

        String randomName = "Test Food" + Math.ceil(Math.random()*100);
        UpdateItemRequestDto requestDto = new UpdateItemRequestDto("", randomName, "", "", "", 500, 1000, 10002, false);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto, item.getId());
        UpdateItemResponseDto response = extractableResponse.as(UpdateItemResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");

        deleteTestData(item.getId());
    }

    @Test
    @DisplayName("실패 : 권한 없음")
    public void updateItemByUser() {
        Item item = saveItem(createItemRequestDto(true));

        String randomName = "Test Food" + Math.ceil(Math.random()*100);
        UpdateItemRequestDto requestDto = new UpdateItemRequestDto("", randomName, "", "", "", 500, 1000, 10002, false);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", requestDto, item.getId());
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");

        deleteTestData(item.getId());
    }

    @Test
    @DisplayName("실패 : 인증되지 않은 사용자")
    public void updateItemByElse() {
        Item item = saveItem(createItemRequestDto(true));

        String randomName = "Test Food" + Math.ceil(Math.random()*100);
        UpdateItemRequestDto requestDto = new UpdateItemRequestDto("", randomName, "", "", "", 500, 1000, 10002, false);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", requestDto, item.getId());
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");

        deleteTestData(item.getId());
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 아이템")
    public void updateItemByNotExist() {
        UpdateItemRequestDto requestDto = new UpdateItemRequestDto("", "TestFood 이름 변경", "", "", "", 500, 1000, 10002, false);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto, 1000000);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(404);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("존재하지 않는 아이템입니다.");
    }
}
