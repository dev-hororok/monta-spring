package com.hororok.monta.dto.request.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateItemRequestDto {
    @JsonProperty("item_type")
    private String itemType;

    private String name;
    private String grade;
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    private Integer cost;

    @JsonProperty("required_study_time")
    private Integer requiredStudyTime;

    @JsonProperty("effect_code")
    private Integer effectCode;

    @JsonProperty("is_hidden")
    private Boolean isHidden;
}
