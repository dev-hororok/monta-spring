package com.hororok.monta.dto.request.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PatchItemRequestDto {

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
    private boolean isHidden;


}
