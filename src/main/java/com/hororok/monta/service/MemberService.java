package com.hororok.monta.service;

import com.hororok.monta.dto.request.member.PatchMemberRequestDto;
import com.hororok.monta.dto.response.DeleteResponseDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.member.*;
import com.hororok.monta.entity.Character;
import com.hororok.monta.entity.*;
import com.hororok.monta.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final EggInventoryRepository eggInventoryRepository;
    private final CharacterRepository characterRepository;
    private final CharacterInventoryRepository characterInventoryRepository;

    private final Random random = new Random();
    public MemberService(MemberRepository memberRepository, AccountRepository accountRepository, EggInventoryRepository eggInventoryRepository, CharacterRepository characterRepository, CharacterInventoryRepository characterInventoryRepository) {
        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
        this.eggInventoryRepository = eggInventoryRepository;
        this.characterRepository = characterRepository;
        this.characterInventoryRepository = characterInventoryRepository;
    }

    @Transactional
    public ResponseEntity<?> getCurrentMember() {

        String email = getMemberEmail();
        Optional<Member> findMember = findMember(email);

        if(findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new FailResponseDto(HttpStatus.UNAUTHORIZED.name(), Collections.singletonList("가입이 필요합니다.")));
        }

        Member member = findMember.get();
        return ResponseEntity.ok(new GetCurrentMemberResponseDto(member));
    }

    @Transactional
    public ResponseEntity<?> getMembers() {
        List<Member> collectMember = memberRepository.findAll();
        return ResponseEntity.ok(new GetMembersResponseDto(collectMember));
    }

    @Transactional
    public ResponseEntity<?> getMember(UUID memberId) {

        Optional<Member> findMember = memberRepository.findOneById(memberId);

        if(findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 회원입니다.")));
        }

        Member member = findMember.get();
        return ResponseEntity.ok(new GetMemberResponseDto(member));
    }

    @Transactional
    public ResponseEntity<?> postMember() {

        String email = getMemberEmail();
        Optional<Member> findMember = findMember(email);

        if(findMember.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new FailResponseDto(HttpStatus.CONFLICT.name(), Collections.singletonList("이미 가입된 이메일 입니다.")));
        }

        Account accountMember = accountRepository.findOneByEmail(email);

        // 랜덤 닉네임 생성 (UUID 6자리)
        String randomNickname = UUID.randomUUID().toString().substring(0,6);

        UUID saveMemberId = memberRepository.save(new Member(accountMember, randomNickname)).getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PostCreateMemberResponseDto(saveMemberId));

    }

    @Transactional
    public ResponseEntity<?> postFromEggToCharacter(UUID memberId, UUID eggInventoryId) {
        Optional<EggInventory> eggInventoryOpt = eggInventoryRepository.findById(eggInventoryId);

        if (eggInventoryOpt.isEmpty() || !eggInventoryOpt.get().getMember().getId().equals(memberId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("보유하신 달걀이 없습니다.")));
        }

        EggInventory eggInventory = eggInventoryOpt.get();
        if (eggInventory.getProgress() != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("공부 시간이 부족하여 아직 알을 부화할 수 없습니다.")));
        }

        Character eggToCharacter = selectCharacterBasedOnEggGrade(eggInventory.getEgg().getGrade());

        CharacterInventory newCharacterInventory = new CharacterInventory(eggInventory.getMember(), eggToCharacter);
        characterInventoryRepository.save(newCharacterInventory);

        eggInventoryRepository.delete(eggInventory);

        GetEggToCharacterResponseDto.Character characterDto = new GetEggToCharacterResponseDto.Character(
                eggToCharacter.getId().toString(),
                eggToCharacter.getName(),
                eggToCharacter.getDescription(),
                eggToCharacter.getImageUrl(),
                eggToCharacter.getGrade(),
                eggToCharacter.getSellPrice()
        );

        GetEggToCharacterResponseDto.Data data = new GetEggToCharacterResponseDto.Data(characterDto);
        GetEggToCharacterResponseDto responseDto = new GetEggToCharacterResponseDto("success", data);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }


    @Transactional
    public ResponseEntity<?> patchMember(PatchMemberRequestDto requestDto) {

        String email = getMemberEmail();
        Optional<Member> findMember = findMember(email);

        if(findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new FailResponseDto(HttpStatus.UNAUTHORIZED.name(), Collections.singletonList("사용자를 찾을 수 없습니다.")));
        }

        Member member = findMember.get();

        String updateNickname = requestDto.getNickname();
        String updateImageUrl = requestDto.getImageUrl();

        if(requestDto.getNickname()==null) updateNickname = member.getNickname();
        if(requestDto.getImageUrl()==null) updateImageUrl = member.getImageUrl();

        member.updateMember(updateNickname, updateImageUrl);
        Member saveMember = memberRepository.save(member);

        return ResponseEntity.ok(new GetMemberResponseDto(saveMember));
    }

    @Transactional
    public ResponseEntity<?> deleteMember() {

        String email = getMemberEmail();
        Optional<Member> findMember = findMember(email);

        if(findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new FailResponseDto(HttpStatus.UNAUTHORIZED.name(), Collections.singletonList("사용자를 찾을 수 없습니다.")));
        }

        Member member = findMember.get();
        memberRepository.delete(member);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new DeleteResponseDto());
    }

    private Character selectCharacterBasedOnEggGrade(String eggGrade) {
        double randomValue = random.nextDouble();

        String chosenGrade = decideCharacterGrade(eggGrade, randomValue);
        List<Character> charactersOfChosenGrade = characterRepository.findByGrade(chosenGrade);

        return getRandomCharacter(charactersOfChosenGrade);
    }

    private String decideCharacterGrade(String eggGrade, double randomValue) {
        return switch (eggGrade) {
            case "AD" -> randomValue < 0.65 ? "C" : (randomValue < 0.95 ? "B" : "A");
            case "C" -> randomValue < 0.79 ? "C" : (randomValue < 0.99 ? "B" : "A");
            case "B" -> randomValue < 0.79 ? "B" : (randomValue < 0.99 ? "A" : "A+");
            case "A" -> randomValue < 0.90 ? "A" : "A+";
            case "A+" -> randomValue < 0.90 ? "A+" : "S";
            case "S" -> randomValue < 0.90 ? "S" : "S+";
            case "S+" -> randomValue < 0.90 ? "S+" : "SS";
            default -> throw new IllegalStateException("Unexpected egg grade: " + eggGrade);
        };
    }

    private Character getRandomCharacter(List<Character> characters) {
        return characters.get(random.nextInt(characters.size()));
    }

    public Optional<Member> findMember(String email) {
        return memberRepository.findOneByEmail(email);
    }

    public String getMemberEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
