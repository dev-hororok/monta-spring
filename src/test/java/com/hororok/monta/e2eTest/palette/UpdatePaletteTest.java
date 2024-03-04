package com.hororok.monta.e2eTest.palette;

import com.hororok.monta.dto.request.palette.UpdatePaletteRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.palette.UpdatePaletteResponseDto;
import com.hororok.monta.entity.Palette;
import com.hororok.monta.repository.PaletteRepository;
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
public class UpdatePaletteTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PaletteRepository paletteRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    void rollBackData(Palette existingPalette) {
        Optional<Palette> findPalette = paletteRepository.findById(existingPalette.getId());
        if(findPalette.isPresent()) {
            Palette palette = findPalette.get();
            palette.updatePalette(existingPalette.getName(), existingPalette.getGrade(), existingPalette.getLightColor(),
                    existingPalette.getNormalColor(),existingPalette.getDarkColor(), existingPalette.getDarkerColor());
            paletteRepository.save(palette);
        }
    }

    Palette findPalette() {
        List<Palette> palettes = paletteRepository.findAll();
        return palettes.get(0);
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, UpdatePaletteRequestDto requestDto, boolean isExist) {
        String url = "/admin/palettes/" + findPalette().getId();

        if(!isExist) {
            url = "/admin/palettes/1000";
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
    public void updatePaletteByAdmin() {
        Optional<Palette> findPalette = paletteRepository.findById(findPalette().getId());
        Palette existingPalette = findPalette.get();

        UpdatePaletteRequestDto requestDto = new UpdatePaletteRequestDto("Test 이름 변경", "", "", "", "", "");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto, true);
        UpdatePaletteResponseDto response = extractableResponse.as(UpdatePaletteResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getData().getPalette().getName()).isEqualTo("Test 이름 변경");
        assertThat(response.getData().getPalette().getGrade()).isEqualTo(existingPalette.getGrade());

        rollBackData(existingPalette);
    }

    @Test
    @DisplayName("실패 : 권한 없음")
    public void updatePaletteByUser() {
        UpdatePaletteRequestDto requestDto = new UpdatePaletteRequestDto("Test 이름 변경", "", "", "", "", "");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", requestDto, true);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");
    }

    @Test
    @DisplayName("실패 : 인증되지 않은 사용자")
    public void updatePaletteByElse() {
        UpdatePaletteRequestDto requestDto = new UpdatePaletteRequestDto("Test 이름 변경", "", "", "", "", "");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", requestDto, true);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 팔레트")
    public void updatePaletteByNotExist() {
        UpdatePaletteRequestDto requestDto = new UpdatePaletteRequestDto("Test 이름 변경", "", "", "", "", "");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto, false);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(404);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 팔레트를 찾을 수 없습니다.");
    }
}
