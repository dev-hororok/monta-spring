package com.hororok.monta.repository;

import com.hororok.monta.entity.Palette;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaletteRepository extends JpaRepository<Palette, Integer> {
    List<Palette> findAllByGrade(String grade);
}
