package com.hororok.monta.e2e.item;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.item.GetItemResponseDto;
import com.hororok.monta.entity.Item;
import com.hororok.monta.repository.ItemTestRepository;
import com.hororok.monta.setting.TestSetting;
import com.hororok.monta.utils.ItemUtils;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class GetItemTest extends ItemUtils {
    @LocalServerPort
    private int port;

    @Autowired
    ItemTestRepository itemTestRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, int itemId) {
        String url = "/v2/admin/items/" + itemId;

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .when().get(url)
                .then().log().all().extract();
    }

    @DisplayName("성공")
    @Test
    public void getItemByAdmin() {
        Item item = saveItem(createItemRequestDto(true));

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", item.getId());
        GetItemResponseDto response = extractableResponse.as(GetItemResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");

        deleteTestData(item.getId());
    }

    @DisplayName("실패 : 권한 없음")
    @Test
    public void getItemByUser() {
        Item item = saveItem(createItemRequestDto(true));

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", item.getId());
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");

        deleteTestData(item.getId());
    }

    @DisplayName("실패 : 인증되지 않은 사용자")
    @Test
    public void getItemByElse() {
        Item item = saveItem(createItemRequestDto(true));

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", item.getId());
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");

        deleteTestData(item.getId());
    }

    @DisplayName("실패 : 존재하지 않는 아이템")
    @Test
    public void getItemByNotExist() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", 100000);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(404);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("존재하지 않는 아이템입니다.");
    }
}
