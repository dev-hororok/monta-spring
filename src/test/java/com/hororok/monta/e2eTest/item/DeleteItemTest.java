package com.hororok.monta.e2eTest.item;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.entity.Item;
import com.hororok.monta.repository.ItemRepository;
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
public class DeleteItemTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ItemRepository ItemRepository;

    @Autowired
    private ItemTestRepository itemTestRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    Item findItem() {
        List<Item> Items = ItemRepository.findAll();
        return Items.get(0);
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, boolean isExist) {
        String url = "/v2/admin/items/" + findItem().getId();

        if(!isExist) {
            url = "/v2/admin/items/100000";
        }

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .when().delete(url)
                .then().log().all().extract();
    }

    @Test
    @DisplayName("성공")
    public void deleteItemByAdmin() {
        Item item = findItem();

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", true);

        assertThat(extractableResponse.statusCode()).isEqualTo(204);

        itemTestRepository.setDeletedAtNullById(item.getId());
    }

    @Test
    @DisplayName("실패 : 권한 없음")
    public void deleteItemByUser() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", true);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");
    }

    @Test
    @DisplayName("실패 : 인증되지 않은 사용자")
    public void deleteItemByElse() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", true);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 팔레트")
    public void deleteItemByNotExist() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", false);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(404);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("존재하지 않는 아이템입니다.");
    }
}
