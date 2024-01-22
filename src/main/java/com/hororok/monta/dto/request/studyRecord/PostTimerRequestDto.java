package com.hororok.monta.dto.request.studyRecord;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PostTimerRequestDto {

    @JsonProperty("study_category_id")
    private Long studyCategoryId;

    public PostTimerRequestDto(@JsonProperty("study_category_id") Long studyCategoryId) {
        this.studyCategoryId = studyCategoryId!=null ? studyCategoryId : 0L;
    }

}
