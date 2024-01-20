package com.hororok.monta.dto.response.member;

import com.hororok.monta.entity.Authority;
import com.hororok.monta.entity.Member;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class GetMembersDto {

    private UUID member_id;
    private UUID account_id;
    private String email;
    private String nickname;
    private String image_url;

    @Enumerated(EnumType.STRING)
    private Authority role;

    private int point;
    private long active_record_id;
    private UUID active_egg_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public GetMembersDto(Member member) {
        member_id = member.getId();
        email = member.getEmail();
        nickname = member.getNickname();
        image_url = member.getImageUrl();
        role = member.getRole();
        point = member.getPoint();
        active_record_id = member.getActiveRecordId();
        active_egg_id = member.getActiveEggId();
        created_at = member.getCreatedAt();
        updated_at = member.getUpdatedAt();
    }
}
