package com.hororok.monta.repository;

import com.hororok.monta.entity.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface MemberTestRepository extends CrudRepository<Member, UUID> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE member SET point = 10000 WHERE member_id = 'fac9afb2-bc58-4e3b-bb55-9225416cf2dc'", nativeQuery = true)
    void setPoint();
}
