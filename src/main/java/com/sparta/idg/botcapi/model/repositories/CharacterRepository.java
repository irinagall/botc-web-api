package com.sparta.idg.botcapi.model.repositories;

import com.sparta.idg.botcapi.model.entities.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, String> {
    Character findCharacterByName(String name);
}