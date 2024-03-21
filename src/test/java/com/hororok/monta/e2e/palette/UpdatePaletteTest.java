package com.hororok.monta.e2e.palette;

import com.hororok.monta.dto.request.palette.UpdatePaletteRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.palette.UpdatePaletteResponseDto;
import com.hororok.monta.entity.Palette;
import com.hororok.monta.setting.TestSetting;
import com.hororok.monta.utils.PaletteUtils;
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
public class UpdatePaletteTest extends PaletteUtils {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, UpdatePaletteRequestDto requestDto, int paletteId) {
        String url = "/admin/palettes/" + paletteId;

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().patch(url)
                .then().log().all().extract();
    }

    @Test
    @DisplayName("성공")
    public void updatePaletteByAdmin() {
        Palette palette = savePalette(createPaletteRequestDto(true));

        String randomName = "Test Palette" + Math.ceil(Math.random()*100);
        UpdatePaletteRequestDto requestDto = new UpdatePaletteRequestDto(randomName, "", "", "", "", "");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto, palette.getId());
        UpdatePaletteResponseDto response = extractableResponse.as(UpdatePaletteResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");

        deleteTestData(palette.getId());
    }

    @Test
    @DisplayName("실패 : 권한 없음")
    public void updatePaletteByUser() {
        Palette palette = savePalette(createPaletteRequestDto(true));

        UpdatePaletteRequestDto requestDto = new UpdatePaletteRequestDto("Test 이름 변경", "", "", "", "", "");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", requestDto, palette.getId());
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");

        deleteTestData(palette.getId());
    }

    @Test
    @DisplayName("실패 : 인증되지 않은 사용자")
    public void updatePaletteByElse() {
        Palette palette = savePalette(createPaletteRequestDto(true));

        UpdatePaletteRequestDto requestDto = new UpdatePaletteRequestDto("Test 이름 변경", "", "", "", "", "");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", requestDto, palette.getId());
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");

        deleteTestData(palette.getId());
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 팔레트")
    public void updatePaletteByNotExist() {
        UpdatePaletteRequestDto requestDto = new UpdatePaletteRequestDto("Test 이름 변경", "", "", "", "", "");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto, 10000000);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(404);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 팔레트를 찾을 수 없습니다.");
    }
}
