package com.hororok.monta.e2e.item;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.itemInventory.UsePaletteGachaResponseDto;
import com.hororok.monta.dto.response.itemInventory.UseCharacterGachaResponseDto;
import com.hororok.monta.entity.Item;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.setting.TestSetting;
import com.hororok.monta.utils.*;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UseItemTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ItemUtils itemUtils;

    @Autowired
    private ItemInventoryUtils itemInventoryUtils;

    @Autowired
    private MemberUtils memberUtils;

    @Autowired
    private CharacterInventoryUtils characterInventoryUtils;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, long itemInventoryId) {
        String url = "v2/item-inventory/" + itemInventoryId;

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .when().post(url)
                .then().log().all().extract();
    }

    @Test
    @DisplayName("성공 : Character 뽑기 아이템 사용")
    public void useCharacterGachaItem() {
        Item item = itemUtils.saveItem(itemUtils.createItemRequestDtoByItemTypeAndEffectCode("Food", 10000));
        Member member = memberUtils.findMember(TestSetting.getMemberId());
        ItemInventory itemInventory = itemInventoryUtils.saveItemInventory(item, member, 0);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", itemInventory.getId());
        UseCharacterGachaResponseDto response = extractableResponse.as(UseCharacterGachaResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");

        itemInventoryUtils.deleteTestData(itemInventory.getId());
        itemUtils.deleteTestData(item.getId());
        characterInventoryUtils.deleteTestData(response.getData().getCharacterInventoryId());
    }

    @Test
    @DisplayName("성공 : Palette 뽑기 아이템 사용")
    public void usePaletteGachaItem() {
        Item item = itemUtils.saveItem(itemUtils.createItemRequestDtoByItemTypeAndEffectCode("Consumable", 20001));
        Member member = memberUtils.findMember(TestSetting.getMemberId());
        ItemInventory itemInventory = itemInventoryUtils.saveItemInventory(item, member, 0);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", itemInventory.getId());
        UsePaletteGachaResponseDto response = extractableResponse.as(UsePaletteGachaResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");

        itemInventoryUtils.deleteTestData(itemInventory.getId());
        itemUtils.deleteTestData(item.getId());
    }

    @Test
    @DisplayName("실패 : 잔여 공부 시간 존재")
    public void useFoodItemWithInsufficientTime() {
        Item item = itemUtils.saveItem(itemUtils.createItemRequestDto(true));
        Member member = memberUtils.findMember(TestSetting.getMemberId());
        ItemInventory itemInventory = itemInventoryUtils.saveItemInventory(item, member, 1000);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", itemInventory.getId());
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(400);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("잔여 시간만큼 공부해야 사용할 수 있습니다.");

        itemInventoryUtils.deleteTestData(itemInventory.getId());
        itemUtils.deleteTestData(item.getId());
    }
}
