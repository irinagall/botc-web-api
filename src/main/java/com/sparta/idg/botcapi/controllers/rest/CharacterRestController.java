package com.sparta.idg.botcapi.controllers.rest;

import com.sparta.idg.botcapi.model.entities.Character;
import com.sparta.idg.botcapi.model.repositories.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CharacterRestController {
    private final CharacterRepository characterRepository;

    @Autowired
    public CharacterRestController(CharacterRepository characterRepository){
        this.characterRepository = characterRepository;
    }

    @GetMapping("/rest/characters")
    public List<Character> getAllCharacters(){return characterRepository.findAll();}
}
