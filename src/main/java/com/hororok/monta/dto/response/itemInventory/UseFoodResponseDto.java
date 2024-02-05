package com.hororok.monta.dto.response.itemInventory;

import com.hororok.monta.entity.Character;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UseFoodResponseDto {

    private String status;
    private Data data;

    public UseFoodResponseDto(Character character) {
        this.status = "success";
        this.data = new Data(character);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private Character character;
    }

}
