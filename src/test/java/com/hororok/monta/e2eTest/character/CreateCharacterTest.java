package com.hororok.monta.e2eTest.character;

import com.hororok.monta.dto.request.character.CreateCharacterRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.character.CreateCharacterResponseDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.repository.CharacterRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class CreateCharacterTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CharacterRepository CharacterRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    void rollBackData(int CharacterId) {
        Optional<Character> findCharacter = CharacterRepository.findById(CharacterId);
        if(findCharacter.isPresent()) {
            Character Character = findCharacter.get();
            CharacterRepository.delete(Character);
        }
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, CreateCharacterRequestDto requestDto) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().post("/admin/characters")
                .then().log().all().extract();
    }

    @Test
    @DisplayName("성공")
    public void createCharacterByAdmin() {
        CreateCharacterRequestDto requestDto = new CreateCharacterRequestDto("Test Character", "테스트 캐릭터", "TestCharacterUrl", "B", 200);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        CreateCharacterResponseDto response = extractableResponse.as(CreateCharacterResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(201);
        assertThat(response.getStatus()).isEqualTo("success");

        rollBackData(response.getData().getCharacterId());
    }

    @Test
    @DisplayName("실패 : 권한 없음")
    public void createCharacterByUser() {
        CreateCharacterRequestDto requestDto = new CreateCharacterRequestDto("Test Character", "테스트 캐릭터", "TestCharacterUrl", "B", 200);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");
    }

    @Test
    @DisplayName("실패 : 인증되지 않은 사용자")
    public void createCharacterByElse() {
        CreateCharacterRequestDto requestDto = new CreateCharacterRequestDto("Test Character", "테스트 캐릭터", "TestCharacterUrl", "B", 200);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }

    @Test
    @DisplayName("실패 : 유효성 에러")
    public void createCharacterByBlank() {
        CreateCharacterRequestDto requestDto = new CreateCharacterRequestDto("", "", "", "", 200);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(400);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("name : 캐릭터 이름은 필수 입력 값 입니다.");
    }
}
