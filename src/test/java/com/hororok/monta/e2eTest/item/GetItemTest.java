package com.hororok.monta.e2eTest.item;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.item.GetItemResponseDto;
import com.hororok.monta.entity.Item;
import com.hororok.monta.repository.ItemTestRepository;
import com.hororok.monta.setting.TestSetting;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class GetItemTest {
    @LocalServerPort
    private int port;

    @Autowired
    ItemTestRepository itemTestRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    Item findItem() {
        List<Item> items = (List<Item>) itemTestRepository.findAll();
        return items.get(0);
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, boolean isExist) {
        String url = "/v2/admin/items/" + findItem().getId();

        if(!isExist) {
            url = "/v2/admin/items/1000000";
        }

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .when().get(url)
                .then().log().all().extract();
    }

    @DisplayName("성공")
    @Test
    public void getItemByAdmin() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", true);
        GetItemResponseDto response = extractableResponse.as(GetItemResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");
    }

    @DisplayName("실패 : 권한 없음")
    @Test
    public void getItemByUser() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", true);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");
    }

    @DisplayName("실패 : 인증되지 않은 사용자")
    @Test
    public void getItemByElse() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", true);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }

    @DisplayName("실패 : 존재하지 않는 아이템")
    @Test
    public void getItemByNotExist() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", false);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(404);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("존재하지 않는 아이템입니다.");
    }
}
