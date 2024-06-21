package com.sparta.idg.botcapi.controllers.rest;

import com.sparta.idg.botcapi.model.entities.Character;
import com.sparta.idg.botcapi.model.repositories.CharacterRepository;
import com.sparta.idg.botcapi.model.repositories.ScriptRepository;
import com.sparta.idg.botcapi.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RequestMapping("/rest")
@RestController
public class CharacterRestController {
    private final ScriptRepository scriptRepository;
    private CharacterRepository characterRepository ;
    private CharacterService characterService;

    @Autowired
    public CharacterRestController(CharacterRepository characterRepository, ScriptRepository scriptRepository, CharacterService characterService){
        this.characterRepository = characterRepository;
        this.scriptRepository = scriptRepository;
        this.characterService = characterService;
    }

    @GetMapping("/characters")
    public List<Character> getAllCharacters(){return characterRepository.findAll();}

    @GetMapping("/character/name")
    public List<Character> getCharacterByName(@RequestParam(name="name", required = false) String name){
        return characterRepository.findAll().stream().filter(character -> character.getName().contains(name)).toList();
    }

    @GetMapping("/characters/{id}")
    public Character getCharacterById(@PathVariable String id){
        return characterRepository.findCharacterByName(id);
    }

    @PostMapping("/characters")
    public String addCharacter(@RequestBody Character character){
        characterRepository.save(character);
        return " New character added: " + character.getName();
    }

    @DeleteMapping("characters/{name}")
    public String deleteCharacterByName(@PathVariable String name){
        if(characterRepository.existsById(name)){
            characterRepository.deleteById(name);
            return "You have deleted the character with name identifier: " + name;
        }else{
            return "Couldn't find character with name identifier " + name + " therefore, it could not be deleted.";
        }
    }

    @PutMapping("characters/{name}")
    public Character updateCharacterAbilityByName(@PathVariable String name, @RequestBody Character character){
        //Get the character that needs to be updated
        Character characterToUpdate = characterRepository.findById(name).get();
        //Update the character object with the changes
        characterToUpdate.setAbility(character.getAbility());
        //Save it to the Database
        return characterRepository.save(characterToUpdate);
    }

}
