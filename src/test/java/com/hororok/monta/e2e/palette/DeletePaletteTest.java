package com.hororok.monta.e2e.palette;

import com.hororok.monta.dto.response.FailResponseDto;
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
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class DeletePaletteTest extends PaletteUtils {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, int paletteId) {
        String url = "/admin/palettes/" + paletteId;

        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .when().delete(url)
                .then().log().all().extract();
    }

    @Test
    @DisplayName("성공")
    public void deletePaletteByAdmin() {
        Palette palette = savePalette(createPaletteRequestDto(true));

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", palette.getId());

        assertThat(extractableResponse.statusCode()).isEqualTo(204);

        deleteTestData(palette.getId());
    }

    @Test
    @DisplayName("실패 : 권한 없음")
    public void deletePaletteByUser() {
        Palette palette = savePalette(createPaletteRequestDto(true));

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", palette.getId());
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");

        deleteTestData(palette.getId());
    }

    @Test
    @DisplayName("실패 : 인증되지 않은 사용자")
    public void deletePaletteByElse() {
        Palette palette = savePalette(createPaletteRequestDto(true));

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", palette.getId());
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");

        deleteTestData(palette.getId());
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 팔레트")
    public void deletePaletteByNotExist() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", 100000);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(404);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 팔레트를 찾을 수 없습니다.");
    }
}
