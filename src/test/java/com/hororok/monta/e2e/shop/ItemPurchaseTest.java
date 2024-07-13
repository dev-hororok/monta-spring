package com.hororok.monta.e2e.shop;

import com.hororok.monta.dto.request.shop.PurchaseRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.shop.TransactionResponseDto;
import com.hororok.monta.entity.Item;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.repository.*;
import com.hororok.monta.setting.TestSetting;
import com.hororok.monta.utils.ItemInventoryUtils;
import com.hororok.monta.utils.ItemUtils;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ItemPurchaseTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ItemUtils itemUtils;

    @Autowired
    private MemberUtils memberUtils;

    @Autowired
    private TransactionRecordUtils transactionRecordUtils;

    @Autowired
    private ItemInventoryUtils itemInventoryUtils;

    @Autowired
    private ItemInventoryTestRepository itemInventoryTestRepository;


    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @BeforeEach
    void setPoint() {
        memberUtils.setPoint();
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, PurchaseRequestDto requestDto) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().post("/v2/shop/purchase")
                .then().log().all().extract();
    }

    @Test
    @DisplayName("성공 : Food 구매")
    public void purchaseFood() {
        Item item = itemUtils.saveItem(itemUtils.createItemRequestDtoByItemType("Food"));

        List<ItemInventory> itemInventoryList = itemInventoryTestRepository.findAllByMemberIdAndItemType(TestSetting.getMemberId(), "Food");

        for(ItemInventory itemInventory : itemInventoryList) {
            itemInventoryTestRepository.deleteTestData(itemInventory.getId());
        }

        PurchaseRequestDto requestDto = new PurchaseRequestDto(item.getId(), 1);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        TransactionResponseDto response = extractableResponse.as(TransactionResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(201);
        assertThat(response.getStatus()).isEqualTo("success");

        transactionRecordUtils.deleteTestData(response.getData().getTransactionRecord().getTransactionRecordId());
        ItemInventory itemInventory = itemInventoryTestRepository.findOneByMemberIdAndItemType(TestSetting.getMemberId(), "Food");
        itemInventoryUtils.deleteTestData(itemInventory.getId());
        itemUtils.deleteTestData(item.getId());
    }

    @Test
    @DisplayName("성공 : Consumable 구매")
    public void purchaseConsumable() {
        Item item = itemUtils.saveItem(itemUtils.createItemRequestDtoByItemType("Consumable"));

        PurchaseRequestDto requestDto = new PurchaseRequestDto(item.getId(), 1);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        TransactionResponseDto response = extractableResponse.as(TransactionResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(201);
        assertThat(response.getStatus()).isEqualTo("success");

        transactionRecordUtils.deleteTestData(response.getData().getTransactionRecord().getTransactionRecordId());
        ItemInventory itemInventory = itemInventoryTestRepository.findByMemberIdAndItemId(TestSetting.getMemberId(), item.getId());
        itemInventoryUtils.deleteTestData(itemInventory.getId());
        itemUtils.deleteTestData(item.getId());
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 item")
    public void purchaseByNotExistItem() {
        PurchaseRequestDto requestDto = new PurchaseRequestDto(10000000, 1);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(404);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("존재하지 않는 Item 입니다.");
    }

    @Test
    @DisplayName("실패 : 수량 오류")
    public void purchaseByCountShortage() {
        Item item = itemUtils.saveItem(itemUtils.createItemRequestDtoByItemType("Consumable"));

        PurchaseRequestDto requestDto = new PurchaseRequestDto(item.getId(), 0);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(400);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("수량을 1개 이상 선택해주세요.");

        itemUtils.deleteTestData(item.getId());
    }
}
