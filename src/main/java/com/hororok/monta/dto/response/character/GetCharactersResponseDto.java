package com.hororok.monta.dto.response.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Character;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetCharactersResponseDto {
    private String status;
    private Data data;

    public GetCharactersResponseDto(List<Character> characters) {
        this.status = "success";
        this.data = new Data(convertToDtoList(characters));
    }

    private List<GetCharacterResponseDto.GetCharacterDto> convertToDtoList(List<Character> characters) {
        List<GetCharacterResponseDto.GetCharacterDto> list = new ArrayList<>();
        for(Character character : characters) {
            list.add(new GetCharacterResponseDto.GetCharacterDto(character));
        }
        return list;
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private List<GetCharacterResponseDto.GetCharacterDto> characters;
    }
}
