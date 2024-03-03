package com.hororok.monta.e2eTest.palette;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hororok.monta.dto.request.palette.CreatePaletteRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.palette.CreatePaletteResponseDto;
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
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class PostPaletteTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PaletteRepository paletteRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    void rollBackData(int paletteId) {
        Optional<Palette> findPalette = paletteRepository.findById(paletteId);
        if(findPalette.isPresent()) {
            Palette palette = findPalette.get();
            paletteRepository.delete(palette);
        }
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, CreatePaletteRequestDto requestDto) throws JsonProcessingException {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().post("/admin/palettes")
                .then().log().all().extract();
    }

    @Test
    @DisplayName("성공")
    public void postPalettesByAdmin() throws Exception {
        CreatePaletteRequestDto requestDto = new CreatePaletteRequestDto("Test", "Rare", "#000000", "#000001", "#000002", "#000003");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        CreatePaletteResponseDto response = extractableResponse.as(CreatePaletteResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(201);
        assertThat(response.getStatus()).isEqualTo("success");

        rollBackData(response.getData().getPaletteId());
    }

    @Test
    @DisplayName("실패 : 권한 없음")
    public void postPaletteByUser() throws Exception {
        CreatePaletteRequestDto requestDto = new CreatePaletteRequestDto("Test","Rare","#000000", "#000001", "#000002","#000003");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");
    }

    @Test
    @DisplayName("실패 : 인증되지 않은 사용자")
    public void postPaletteByElse() throws Exception {
        CreatePaletteRequestDto requestDto = new CreatePaletteRequestDto("Test","Rare","#000000", "#000001", "#000002","#000003");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }

    @Test
    @DisplayName("실패 : 유효성 에러")
    public void postPalettesByBlank() throws Exception {
        CreatePaletteRequestDto requestDto = new CreatePaletteRequestDto("","","", "", "","");

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(400);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("name : 팔레트 이름은 필수 입력 값 입니다.");
    }
}
