package com.hororok.monta.dto.response.itemInventory;

import com.hororok.monta.entity.Palette;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UseConsumableResponseDto {

    private String status;
    private Data data;

    public UseConsumableResponseDto(Palette palette) {
        this.status = "success";
        this.data = new Data(palette);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private Palette palette;
    }

}
