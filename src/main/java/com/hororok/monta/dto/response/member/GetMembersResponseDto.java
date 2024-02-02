package com.hororok.monta.dto.response.member;

import com.hororok.monta.dto.response.palette.GetPalettesResponseDto;
import com.hororok.monta.entity.Authority;
import com.hororok.monta.entity.Member;
import com.hororok.monta.entity.Palette;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class GetMembersResponseDto {

    private String status;
    private Data data;

    public GetMembersResponseDto(List<Member> members) {
        this.status = "success";
        this.data = new Data(convertToDtoList(members));
    }

    private List<GetMembersDto> convertToDtoList(List<Member> members) {
        List<GetMembersDto> list = new ArrayList<>();
        for(Member member : members) {
            list.add(new GetMembersDto(member));
        }
        return list;
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private List<GetMembersDto> members;
    }

    @Getter
    @AllArgsConstructor
    public static class GetMembersDto {

        private UUID member_id;
        private UUID account_id;
        private String email;
        private String nickname;
        private String image_url;

        @Enumerated(EnumType.STRING)
        private Authority role;

        private int point;
        private Long active_record_id;
        private UUID active_egg_id;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;

        public GetMembersDto(Member member) {
            member_id = member.getId();
            account_id = member.getAccountId();
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
}
