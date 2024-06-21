package com.sparta.idg.botcapi.service;

import com.sparta.idg.botcapi.model.entities.Character;
import com.sparta.idg.botcapi.model.repositories.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {
    private final CharacterRepository characterRepository;

    @Autowired
    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

}
