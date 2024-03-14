package com.hororok.monta.e2e.item;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.itemInventory.UseConsumableResponseDto;
import com.hororok.monta.dto.response.itemInventory.UseFoodResponseDto;
import com.hororok.monta.entity.Item;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.ItemInventoryTestRepository;
import com.hororok.monta.repository.ItemTestRepository;
import com.hororok.monta.repository.MemberTestRepository;
import com.hororok.monta.setting.TestSetting;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UseItemTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ItemInventoryTestRepository itemInventoryTestRepository;

    @Autowired
    private ItemTestRepository itemTestRepository;

    @Autowired
    private MemberTestRepository memberTestRepository;

    private static Item foodItem;
    private static Item consumableItem;

    private static Member member;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @BeforeAll
    void setItemAndMember() {
        Optional<Item> findFoodItem = itemTestRepository.findById(1);
        foodItem = findFoodItem.get();

        Optional<Item> findConsumableItem = itemTestRepository.findById(2);
        consumableItem = findConsumableItem.get();

        Optional<Member> findMember = memberTestRepository.findById(TestSetting.getMemberId());
        member = findMember.get();
    }

    ItemInventory itemInventorySetting(String itemType, int requiredStudyTime) {
        ItemInventory itemInventory = null;

        if(itemType.equals("Food")) {
            itemInventory = new ItemInventory(9999999L, foodItem, member, foodItem.getItemType(), requiredStudyTime, 1);
        } else {
            itemInventory = new ItemInventory(9999999L, consumableItem, member, consumableItem.getItemType(), requiredStudyTime, 1);
        }
        return itemInventoryTestRepository.save(itemInventory);
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, ItemInventory itemInventory, boolean isExist) {
        String url = "v2/item-inventory/" + itemInventory.getId();

        if(!isExist) {
            url = "v2/item-inventory/1000000000";
        }

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .when().post(url)
                .then().log().all().extract();
    }

    @Test
    @DisplayName("성공 : Food 사용")
    public void useFoodItem() {
        ItemInventory itemInventory = itemInventorySetting("Food", 0);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", itemInventory, true);
        UseFoodResponseDto response = extractableResponse.as(UseFoodResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");

        itemInventoryTestRepository.deleteTestData(itemInventory.getId());
    }

    @Test
    @DisplayName("성공 : Consumable 사용")
    public void useConsumableItem() {
        ItemInventory itemInventory = itemInventorySetting("Consumable", 0);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", itemInventory,true);
        UseConsumableResponseDto response = extractableResponse.as(UseConsumableResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");

        itemInventoryTestRepository.deleteTestData(itemInventory.getId());
    }

    @Test
    @DisplayName("실패 : 잔여 공부 시간 존재")
    public void useFoodItemWithInsufficientTime() {
        ItemInventory itemInventory = itemInventorySetting("Food", 100);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", itemInventory,true);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(400);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("잔여 시간만큼 공부해야 사용할 수 있습니다.");
    }
}
