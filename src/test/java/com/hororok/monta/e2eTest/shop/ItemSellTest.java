package com.hororok.monta.e2eTest.shop;

import com.hororok.monta.dto.request.shop.SellRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.shop.TransactionResponseDto;
import com.hororok.monta.entity.CharacterInventory;
import com.hororok.monta.repository.CharacterInventoryRepository;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ItemSellTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CharacterInventoryRepository characterInventoryRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    UUID getMemberId() {
        return UUID.fromString("efb60e2d-b92b-42d2-a5fa-9f3706c1b2c7");
    }

    CharacterInventory characterInventorySetting() {
        Optional<CharacterInventory> findCharacterInventory = characterInventoryRepository.findOneByMemberId(getMemberId());
        return findCharacterInventory.get();
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, SellRequestDto requestDto) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().post("/v2/shop/sell")
                .then().log().all().extract();
    }

    @Test
    @DisplayName("성공")
    public void sellCharacter() {
        CharacterInventory characterInventory = characterInventorySetting();

        SellRequestDto requestDto = new SellRequestDto(characterInventory.getId(), 1);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        TransactionResponseDto response = extractableResponse.as(TransactionResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(201);
        assertThat(response.getStatus()).isEqualTo("success");
    }

    @Test
    @DisplayName("실패 : 보유하지 않은 Character")
    public void sellByNotPossessionCharacter() {
        SellRequestDto requestDto = new SellRequestDto(100000, 1);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(404);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("보유하고 있는 캐릭터로 판매 요청해주세요.");
    }

    @Test
    @DisplayName("실패 : 수량 오류")
    public void sellByCountShortage() {
        CharacterInventory characterInventory = characterInventorySetting();

        SellRequestDto requestDto = new SellRequestDto(characterInventory.getId(), 0);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(400);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("수량을 1개 이상 선택해주세요.");
    }
}
