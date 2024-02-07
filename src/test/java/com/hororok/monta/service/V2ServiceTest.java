//package com.hororok.monta.service;
//
//import com.hororok.monta.dto.request.shop.PurchaseRequestDtoV2;
//import com.hororok.monta.entity.Member;
//import jakarta.persistence.EntityManager;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class V2ServiceTest {
//
//    @Autowired EntityManager em;
//    @Autowired V2Service v2Service;
//
//    @Nested
//    @DisplayName("구매 테스트")
//    class testPurchase() {
//
//        @Test
//        @DisplayName("실패 (존재하지 않는 Item_id)")
//        public void failByItemId() {
//            //given
//            PurchaseRequestDtoV2 requestDtoV2 = createPurchaseRequestDtoV2();
//
//            //when
//            ResponseEntity<?> responseEntity = v2Service.postPurchase(requestDtoV2);
//
//            //then
//            Assertions.assertThat("존재하지 않는 Item 입니다.").isEqualTo(responseEntity.getBody().);
//
//        }
//
//
//    }
//
//    private Member createMember() {
//        new Member()
//    }
//
//    private PurchaseRequestDtoV2 createPurchaseRequestDtoV2() {
//        return new PurchaseRequestDtoV2(2000L, 1);
//    }
//
//}