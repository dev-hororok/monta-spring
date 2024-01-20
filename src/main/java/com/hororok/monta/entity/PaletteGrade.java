package com.hororok.monta.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum PaletteGrade {
    RARE, EPIC, LEGENDARY;

    // 입력되는 값을 대문자로 변환하여 일치하는 Grade가 있으면 반환, 없다면 null 반환
    @JsonCreator
    public static PaletteGrade parsing(String inputValue) {
        return Stream.of(PaletteGrade.values()).filter(paletteGrade -> paletteGrade.toString().equals(inputValue.toUpperCase()))
                .findFirst().orElse(null);
    }
}
