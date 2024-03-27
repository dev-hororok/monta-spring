package com.hororok.monta.e2e.shop;

import com.hororok.monta.dto.request.shop.SellRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.shop.TransactionResponseDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.entity.CharacterInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.CharacterInventoryTestRepository;
import com.hororok.monta.setting.TestSetting;
import com.hororok.monta.utils.CharacterInventoryUtils;
import com.hororok.monta.utils.CharacterUtils;
import com.hororok.monta.utils.MemberUtils;
import com.hororok.monta.utils.TransactionRecordUtils;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ItemSellTest {
    @LocalServerPort
    private int port;

    @Autowired
    private CharacterUtils characterUtils;

    @Autowired
    private CharacterInventoryUtils characterInventoryUtils;

    @Autowired
    private MemberUtils memberUtils;

    @Autowired
    private TransactionRecordUtils transactionRecordUtils;

    @Autowired
    private CharacterInventoryTestRepository characterInventoryTestRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @BeforeEach
    void setQuantity() {
        characterInventoryTestRepository.setQuantity();
    }

    CharacterInventory characterInventorySetting() {
        Optional<CharacterInventory> findCharacterInventory = characterInventoryTestRepository.findById(1);
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
        Character character = characterUtils.saveCharacter(characterUtils.createCharacterRequestDto(true));
        Member member = memberUtils.findMember(TestSetting.getMemberId());
        CharacterInventory characterInventory = characterInventoryUtils.saveCharacterInventory(member, character);

        SellRequestDto requestDto = new SellRequestDto(characterInventory.getId(), 1);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        TransactionResponseDto response = extractableResponse.as(TransactionResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(201);
        assertThat(response.getStatus()).isEqualTo("success");

        transactionRecordUtils.deleteTestData(response.getData().getTransactionRecord().getTransactionRecordId());
        characterInventoryUtils.deleteTestData(characterInventory.getId());
        characterUtils.deleteTestData(character.getId());
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
        Character character = characterUtils.saveCharacter(characterUtils.createCharacterRequestDto(true));
        Member member = memberUtils.findMember(TestSetting.getMemberId());
        CharacterInventory characterInventory = characterInventoryUtils.saveCharacterInventory(member, character);

        SellRequestDto requestDto = new SellRequestDto(characterInventory.getId(), 0);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(400);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("수량을 1개 이상 선택해주세요.");

        characterInventoryUtils.deleteTestData(characterInventory.getId());
        characterUtils.deleteTestData(character.getId());
    }
}
