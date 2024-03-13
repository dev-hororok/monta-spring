package com.hororok.monta.e2e.character;

import com.hororok.monta.dto.request.character.UpdateCharacterRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.character.GetCharacterResponseDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.repository.CharacterTestRepository;
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
public class UpdateCharacterTest {
    @LocalServerPort
    private int port;

    @Autowired
    private CharacterTestRepository characterTestRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    void rollBackData(Character existingCharacter) {
        Optional<Character> findCharacter = characterTestRepository.findById(existingCharacter.getId());
        if(findCharacter.isPresent()) {
            Character character = findCharacter.get();
            character.setName(existingCharacter.getName());
            character.setDescription(existingCharacter.getDescription());
            character.setImageUrl(existingCharacter.getImageUrl());
            character.setGrade(existingCharacter.getGrade());
            character.setSellPrice(existingCharacter.getSellPrice());
            characterTestRepository.save(character);
        }
    }

    Character findCharacter() {
        List<Character> Characters = (List<Character>) characterTestRepository.findAll();
        return Characters.get(0);
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role, UpdateCharacterRequestDto requestDto, boolean isExist) {
        String url = "/admin/characters/" + findCharacter().getId();

        if(!isExist) {
            url = "/admin/characters/100000";
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
    public void updateCharacterByAdmin() {
        Optional<Character> findCharacter = characterTestRepository.findById(findCharacter().getId());
        Character existingCharacter = findCharacter.get();

        String randomName = "Test Character " + Math.ceil(Math.random()*100);
        UpdateCharacterRequestDto requestDto = new UpdateCharacterRequestDto(randomName, "", "", "", 500);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto, true);
        GetCharacterResponseDto response = extractableResponse.as(GetCharacterResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getData().getGetCharacterDto().getName()).isEqualTo(randomName);
        assertThat(response.getData().getGetCharacterDto().getGrade()).isEqualTo(existingCharacter.getGrade());

        rollBackData(existingCharacter);
    }

    @Test
    @DisplayName("실패 : 권한 없음")
    public void updateCharacterByUser() {
        UpdateCharacterRequestDto requestDto = new UpdateCharacterRequestDto("Test Character " + Math.ceil(Math.random()*100), "", "", "", 500);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User", requestDto, true);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");
    }

    @Test
    @DisplayName("실패 : 인증되지 않은 사용자")
    public void updateCharacterByElse() {
        UpdateCharacterRequestDto requestDto = new UpdateCharacterRequestDto("Test Character " + Math.ceil(Math.random()*100), "", "", "", 500);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else", requestDto, true);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 캐릭터")
    public void updateCharacterByNotExist() {
        UpdateCharacterRequestDto requestDto = new UpdateCharacterRequestDto("Test Character " + Math.ceil(Math.random()*100), "", "", "", 500);

        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin", requestDto, false);
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(400);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("캐릭터를 찾을 수 없습니다.");
    }
}
