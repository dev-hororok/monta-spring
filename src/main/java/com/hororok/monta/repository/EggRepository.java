package com.hororok.monta.repository;

import com.hororok.monta.entity.Egg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EggRepository extends JpaRepository<Egg, UUID> {
}
