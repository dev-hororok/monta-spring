package com.hororok.monta.service;

import com.hororok.monta.dto.request.studyRecord.PostTimerRequestDto;
import com.hororok.monta.dto.response.DeleteResponseDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.studyRecord.PostTimerResponseDto;
import com.hororok.monta.entity.EggInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.entity.StudyCategory;
import com.hororok.monta.entity.StudyRecord;
import com.hororok.monta.repository.EggInventoryRepository;
import com.hororok.monta.repository.MemberRepository;
import com.hororok.monta.repository.StudyCategoryRepository;
import com.hororok.monta.repository.StudyRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TimerService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final StudyRecordRepository studyRecordRepository;
    private final StudyCategoryRepository studyCategoryRepository;
    private final EggInventoryRepository eggInventoryRepository;

    @Transactional
    public ResponseEntity<?> postTimerStart(PostTimerRequestDto requestDto) {

        List<String> errors = new ArrayList<>();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        long studyCategoryId = requestDto.getStudyCategoryId();

        // 카테고리 선택 여부 체크 (0일 경우 에러 반환)
        if(studyCategoryId==0L) {
            errors.add("카테고리를 선택해주세요.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponseDto("카테고리 선택 필요", errors));
        }

        // Account 정보 가져 와서 Member 가입 되어 있는지 체크
        Optional<Member> findMember = memberService.findMember(email);
        if(findMember.isEmpty()) {
            errors.add("유효하지 않은 유저입니다. 가입 후 사용해주세요.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailResponseDto("유효하지 않은 유저", errors));
        }
        Member member = findMember.get();

        // 진행중인 스터디가 있는지 체크
        if(member.getActiveRecordId()!=0) {
            errors.add("진행중인 스터디 종료후에 시작 해주세요.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponseDto("진행중인 스터디 존재", errors));
        }

        // 존재하지 않는 카테고리 or 해당 멤버의 카테고리가 맞는지 체크
        Optional<StudyCategory> findCategory = studyCategoryRepository.findById(studyCategoryId);
        if(findCategory.isEmpty() || findCategory.get().getMember().getId() != findMember.get().getId()) {
            errors.add("유효하지 않은 카테고리입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailResponseDto("유효하지 않은 카테고리", errors));
        }

        // StudyRecord 저장
        StudyRecord saveRecord = studyRecordRepository.save(new StudyRecord(member, findCategory.get()));

        // Member 기록 (active_record_id 업데이트)
        member.updateActiveRecordId(saveRecord.getId());
        memberRepository.save(member);

        return ResponseEntity.status(HttpStatus.CREATED).body(new PostTimerResponseDto("success", null));
    }

    @Transactional
    public ResponseEntity<?> postTimerEnd() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> errors = new ArrayList<>();

        // Account 정보 가져 와서 Member 가입 되어 있는지 체크
        Optional<Member> findMember = memberService.findMember(email);
        if(findMember.isEmpty()) {
            errors.add("유효하지 않은 유저입니다. 가입 후 사용해주세요.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new FailResponseDto("유효하지 않은 유저", errors));
        }
        Member member = findMember.get();

        // 공부중인 카테고리 있는지 체크
        long activeRecordId = member.getActiveRecordId();
        if(activeRecordId == 0) {
            errors.add("진행중인 스터디가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new FailResponseDto("진행중인 스터디 없음", errors));
        }

        // studyRecord - startTime 구하기
        Optional<StudyRecord> findRecord = studyRecordRepository.findById(activeRecordId);
        if(findRecord.isEmpty()) {
            errors.add("진행중인 스터디가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new FailResponseDto("진행중인 스터디 없음", errors));
        }
        StudyRecord studyRecord = findRecord.get();
        LocalDateTime startTime = studyRecord.getCreatedAt();

        // progress 줄여줄 TimeExp 구하기
        long endMilli = System.currentTimeMillis();
        long startMilli = startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        int timeExp = (int) ((endMilli - startMilli) / 1000);

        // StudyRecord 기록 (updatedAt에 종료시간 기록)
        studyRecord.updateDuration(timeExp);
        studyRecordRepository.save(studyRecord);

        // Member 기록 (active_record_id 0으로 update)
        member.updateActiveRecordId(0L);
        memberRepository.save(member);

        // EggInventory - Egg progress 줄여주기 (최대 4개의 알)
        List<EggInventory> eggInventoryList = eggInventoryRepository.findByMemberId(member.getId());
        if(!eggInventoryList.isEmpty()) {
            for (EggInventory eggInventory : eggInventoryList) {
                int progress = eggInventory.getProgress();
                if (progress == 0) continue;
                if (timeExp >= progress) {
                    eggInventory.setProgress(0);
                    timeExp -= progress;
                }
                else {
                    eggInventory.setProgress(progress - timeExp);
                    break;
                }
            }
            eggInventoryRepository.saveAll(eggInventoryList);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new DeleteResponseDto());
    }
}
