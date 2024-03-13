package com.hororok.monta.e2e.item;

import com.hororok.monta.dto.request.item.UpdateItemRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.item.UpdateItemResponseDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class UpdateItemTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ItemTestRepository itemTestRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    void rollBackData(Item existingItem) {
        Optional<Item> findItem = itemTestRepository.findById(existingItem.getId());
        if(findItem.isPresent()) {
            Item item = findItem.get();
            item.updateItem(existingItem.getItemType(), existingItem.getName(), existingItem.getGrade(), existingItem.getDescription(),
                    existingItem.getImageUrl(),existingItem.getCost(), existingItem.getRequiredStudyTime(), existingItem.getEffectCode(), existingItem.getIsHidden());
            itemTestRepository.save(item);
        }
    }

    Item findItem() {
        List<Item> items = (List<Item>) itemTestRepository.findAll();
        return items.get(0);
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, UpdateItemRequestDto requestDto, boolean isExist) {
        String url = "/v2/admin/items/" + findItem().getId();

        if(!isExist) {
            url = "/v2/admin/items/100000";
        }

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
        Optional<Item> findItem = itemTestRepository.findById(findItem().getId());
        Item existingItem = findItem.get();

        String randomName = "Test Food" + Math.ceil(Math.random()*100);
        UpdateItemRequestDto requestDto = new UpdateItemRequestDto("", randomName, "", "", "", 500, 1000, 10002, false);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto, true);
        UpdateItemResponseDto response = extractableResponse.as(UpdateItemResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getData().getItem().getName()).isEqualTo(randomName);
        assertThat(response.getData().getItem().getGrade()).isEqualTo(existingItem.getGrade());

        rollBackData(existingItem);
    }

    @Test
    @DisplayName("실패 : 권한 없음")
    public void updateItemByUser() {
        UpdateItemRequestDto requestDto = new UpdateItemRequestDto("", "TestFood 이름 변경", "", "", "", 500, 1000, 10002, false);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", requestDto, true);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");
    }

    @Test
    @DisplayName("실패 : 인증되지 않은 사용자")
    public void updateItemByElse() {
        UpdateItemRequestDto requestDto = new UpdateItemRequestDto("", "TestFood 이름 변경", "", "", "", 500, 1000, 10002, false);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", requestDto, true);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 아이템")
    public void updateItemByNotExist() {
        UpdateItemRequestDto requestDto = new UpdateItemRequestDto("", "TestFood 이름 변경", "", "", "", 500, 1000, 10002, false);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto, false);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(404);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("존재하지 않는 아이템입니다.");
    }
}
