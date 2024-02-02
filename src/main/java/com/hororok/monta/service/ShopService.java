package com.hororok.monta.service;

import com.hororok.monta.dto.request.shop.PurchaseRequestDto;
import com.hororok.monta.dto.request.shop.SellRequestDto;
import com.hororok.monta.dto.response.shop.PurchaseResponseDto;
import com.hororok.monta.dto.response.shop.SellResponseDto;
import com.hororok.monta.entity.*;
import com.hororok.monta.entity.Character;
import com.hororok.monta.repository.*;
import com.hororok.monta.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ShopService {

    private final EggRepository eggRepository;
    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final EggInventoryRepository eggInventoryRepository;
    private final CharacterInventoryRepository characterInventoryRepository;

    @Autowired
    public ShopService(EggRepository eggRepository, MemberRepository memberRepository, CharacterRepository characterRepository, TransactionRecordRepository transactionRecordRepository, EggInventoryRepository eggInventoryRepository, CharacterInventoryRepository characterInventoryRepository) {
        this.eggRepository = eggRepository;
        this.memberRepository = memberRepository;
        this.characterRepository = characterRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.eggInventoryRepository = eggInventoryRepository;
        this.characterInventoryRepository = characterInventoryRepository;
    }

    @Transactional
    public PurchaseResponseDto purchaseItem(PurchaseRequestDto requestDto) {
        String itemType = requestDto.getItemType();
        UUID itemId = requestDto.getItemId();

        try {
            if ("egg".equals(itemType)) {
                if (itemId == null) {
                    throw new RuntimeException("알의 ID가 필요합니다.");
                }
                return purchaseEgg(itemId, requestDto);
            } else if ("streak_color_change_permission".equals(itemType)) {
                return purchaseStreak(requestDto);
            } else {
                throw new IllegalArgumentException("올바르지 않은 상품 유형입니다.");
            }
        } catch (RuntimeException ex){
            log.error("Purchase failed: {}", ex.getMessage());
            throw ex;
        }
    }

    private PurchaseResponseDto purchaseEgg(UUID eggId, PurchaseRequestDto requestDto) {
        Egg egg = eggRepository.findById(eggId).orElseThrow(() -> new RuntimeException("해당 ID를 갖는 알이 존재하지 않습니다."));

        String currentUsername = SecurityUtil.getCurrentUsername()
                .orElseThrow(() -> new RuntimeException("접근 권한이 없습니다."));

        Member currentMember = memberRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        int existingEggCount = eggInventoryRepository.countByMember(currentMember);
        int requestedCount = requestDto.getCount();

        if (existingEggCount + requestedCount > 4) {
            throw new RuntimeException("알은 최대 4개까지만 보유할 수 있습니다.");
        }
        int totalPurchasePrice = egg.getPurchasePrice() * requestDto.getCount();

        if (currentMember.getPoint() < totalPurchasePrice) {
            throw new RuntimeException("보유하신 포인트가 부족합니다.");
        }

        currentMember.setPoint(currentMember.getPoint() - totalPurchasePrice);

        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setMember(currentMember);
        transactionRecord.setTransactionType("Purchase");
        transactionRecord.setAmount(totalPurchasePrice);
        transactionRecord.setCount(requestDto.getCount());
        transactionRecord.setBalanceAfterTransaction(currentMember.getPoint());
        transactionRecord.setNotes(egg.getName() + " " + requestDto.getCount() + "개 구매");

        transactionRecordRepository.save(transactionRecord);

        for (int i = 0; i < requestDto.getCount(); i++) {
            EggInventory eggInventory = new EggInventory();
            eggInventory.setMember(currentMember);
            eggInventory.setEgg(egg);
            eggInventory.setProgress(egg.getRequiredStudyTime());

            eggInventoryRepository.save(eggInventory);
        }
        return createPurchaseResponseDto(transactionRecord);
    }

    private PurchaseResponseDto purchaseStreak(PurchaseRequestDto requestDto) {
        String currentUsername = SecurityUtil.getCurrentUsername()
                .orElseThrow(() -> new RuntimeException("접근 권한이 없습니다."));

        Member currentMember = memberRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        int count = requestDto.getCount();
        int totalPurchasePrice = count * 15;

        if (currentMember.getPoint() < totalPurchasePrice) {
            throw new RuntimeException("보유하신 포인트가 부족합니다.");
        }

        currentMember.setPoint(currentMember.getPoint() - totalPurchasePrice);

        StreakColorChangePermission streakColorChangePermission = currentMember.getStreakColorChangePermission();

        if (streakColorChangePermission == null) {
            streakColorChangePermission = new StreakColorChangePermission();
            streakColorChangePermission.setMember(currentMember);
            streakColorChangePermission.setAvailableChange(0);
            currentMember.setStreakColorChangePermission(streakColorChangePermission);
        }

        streakColorChangePermission.setAvailableChange(streakColorChangePermission.getAvailableChange() + count);

        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setMember(currentMember);
        transactionRecord.setTransactionType("Purchase");
        transactionRecord.setAmount(totalPurchasePrice);
        transactionRecord.setCount(count);
        transactionRecord.setBalanceAfterTransaction(currentMember.getPoint());
        transactionRecord.setNotes("스트릭 " + count + "개 구매");

        transactionRecordRepository.save(transactionRecord);

        return createPurchaseResponseDto(transactionRecord);
    }

    private PurchaseResponseDto createPurchaseResponseDto(TransactionRecord transactionRecord) {
        PurchaseResponseDto responseDto = new PurchaseResponseDto();
        PurchaseResponseDto.TransactionRecordDto transactionRecordDto = new PurchaseResponseDto.TransactionRecordDto();

        transactionRecordDto.setTransactionRecordId(transactionRecord.getId());
        transactionRecordDto.setTransactionType(transactionRecord.getTransactionType().toString());
        transactionRecordDto.setAmount(transactionRecord.getAmount());
        transactionRecordDto.setCount(transactionRecord.getCount());
        transactionRecordDto.setBalanceAfterTransaction(transactionRecord.getBalanceAfterTransaction());
        transactionRecordDto.setNotes(transactionRecord.getNotes());

        PurchaseResponseDto.Data data = new PurchaseResponseDto.Data();
        data.setTransactionRecord(transactionRecordDto);

        responseDto.setStatus("success");
        responseDto.setData(data);

        return responseDto;
    }

    @Transactional
    public SellResponseDto sellItem(SellRequestDto requestDto) {
        try {
            String currentUsername = SecurityUtil.getCurrentUsername()
                    .orElseThrow(() -> new RuntimeException("접근 권한이 없습니다."));

            Member currentMember = memberRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

            List<CharacterInventory> characterInventories = characterInventoryRepository.findByMemberIdAndCharacterId(currentMember.getId(), requestDto.getItemId());

            if (characterInventories.size() < requestDto.getCount()) {
                throw new RuntimeException("보유하고 있는 캐릭터보다 많은 수량을 입력하셨습니다.");
            }

            Character character = characterRepository.findById(requestDto.getItemId())
                    .orElseThrow(() -> new RuntimeException("해당 캐릭터를 찾을 수 없습니다."));

            int sellPrice = character.getSellPrice() * requestDto.getCount();
            currentMember.setPoint(currentMember.getPoint() + sellPrice);

            for (int i = 0; i < requestDto.getCount(); i++) {
                characterInventoryRepository.delete(characterInventories.get(i));
            }

            TransactionRecord transactionRecord = new TransactionRecord();
            transactionRecord.setMember(currentMember);
            transactionRecord.setTransactionType("Sell");
            transactionRecord.setAmount(sellPrice);
            transactionRecord.setCount(requestDto.getCount());
            transactionRecord.setBalanceAfterTransaction(currentMember.getPoint());
            transactionRecord.setNotes(character.getName() + " " + requestDto.getCount() + "개 판매");

            transactionRecordRepository.save(transactionRecord);

            return createSellResponseDto(transactionRecord);

        } catch (RuntimeException ex) {
            log.error("Error in selling item: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private SellResponseDto createSellResponseDto(TransactionRecord transactionRecord) {
        SellResponseDto.TransactionRecordDto transactionRecordDto = SellResponseDto.TransactionRecordDto.builder()
                .transactionRecordId(transactionRecord.getId())
                .transactionType(transactionRecord.getTransactionType().toString())
                .amount(transactionRecord.getAmount())
                .count(transactionRecord.getCount())
                .balanceAfterTransaction(transactionRecord.getBalanceAfterTransaction())
                .notes(transactionRecord.getNotes())
                .build();

        SellResponseDto.Data data = SellResponseDto.Data.builder()
                .transactionRecord(transactionRecordDto)
                .build();

        return SellResponseDto.builder()
                .status("success")
                .data(data)
                .build();
    }
}