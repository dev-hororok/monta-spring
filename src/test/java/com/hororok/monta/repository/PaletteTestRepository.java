package com.hororok.monta.repository;

import com.hororok.monta.entity.Palette;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PaletteTestRepository extends CrudRepository<Palette, Integer> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM palette WHERE palette_id = :id", nativeQuery = true)
    void deleteTestData(@Param("id") Integer paletteId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE palette SET deleted_at = null WHERE palette_id = :id", nativeQuery = true)
    void setDeletedAtNullById(@Param("id") Integer paletteId);
}
