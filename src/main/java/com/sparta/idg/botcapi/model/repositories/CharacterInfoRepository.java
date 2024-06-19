package com.sparta.idg.botcapi.model.repositories;

import com.sparta.idg.botcapi.model.entities.CharacterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterInfoRepository extends JpaRepository<CharacterInfo, Integer> {
}