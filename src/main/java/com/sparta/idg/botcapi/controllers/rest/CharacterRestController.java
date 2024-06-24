package com.sparta.idg.botcapi.controllers.rest;

import com.sparta.idg.botcapi.model.entities.Character;
import com.sparta.idg.botcapi.model.repositories.CharacterRepository;
import com.sparta.idg.botcapi.model.repositories.ScriptRepository;
import com.sparta.idg.botcapi.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CharacterRestController {
    private final ScriptRepository scriptRepository;
    private CharacterRepository characterRepository;
    private CharacterService characterService;

    @Autowired
    public CharacterRestController(CharacterRepository characterRepository, ScriptRepository scriptRepository, CharacterService characterService) {
        this.characterRepository = characterRepository;
        this.scriptRepository = scriptRepository;
        this.characterService = characterService;
    }

    @GetMapping("/characters")
    public List<Character> getAllCharacters() {
        return characterRepository.findAll();
    }

    @GetMapping("/character/name")
    public CollectionModel<EntityModel<Character>> getCharacterByName(@RequestParam(name = "name", required = false) String name) {
        List<EntityModel<Character>> characterModels = characterRepository.findAll().stream()
                .filter(character -> character.getName().contains(name))
                .map(character -> EntityModel.of(character,
                        linkTo(methodOn(CharacterRestController.class).getCharacterByName(name)).withSelfRel(),
                        linkTo(methodOn(CharacterRestController.class).getAllCharacters()).withRel("allCharacters")))
                .collect(Collectors.toList());

        return CollectionModel.of(characterModels);

    }

    @GetMapping("/characters/{id}")
    public ResponseEntity<EntityModel<Character>> getCharacterById(@PathVariable String id) {
        Character character = characterRepository.findCharacterByName(id);
        if (character == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Character> characterModel = EntityModel.of(character,
                linkTo(methodOn(CharacterRestController.class).getCharacterById(id)).withSelfRel(),
                linkTo(methodOn(CharacterRestController.class).getAllCharacters()).withRel("allCharacters"));

        return ResponseEntity.ok(characterModel);
    }

    @PostMapping("/characters")
    public ResponseEntity<EntityModel<Character>> addCharacter(@RequestBody Character character) {
        characterRepository.save(character);

        EntityModel<Character> characterModel = EntityModel.of(character,
                linkTo(methodOn(CharacterRestController.class).getAllCharacters()).withRel("allCharacters"));

        return ResponseEntity.created(linkTo(methodOn(CharacterRestController.class).getCharacterById(character.getName())).toUri())
                .body(characterModel);
    }

    @DeleteMapping("characters/{name}")
    public ResponseEntity<?> deleteCharacterByName(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        Link allCharactersLink = linkTo(methodOn(this.getClass()).getAllCharacters()).withRel("allCharacters");

        if (characterRepository.existsById(name)) {
            characterRepository.deleteById(name);
            response.put("message", "You have deleted the character with name identifier: " + name);
            return ResponseEntity.ok(EntityModel.of(response, allCharactersLink));
        } else {
            response.put("message", "Couldn't find character with name identifier " + name + " therefore, it could not be deleted.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EntityModel.of(response, allCharactersLink));
        }
    }

    @PutMapping("characters/{name}")
    public ResponseEntity<EntityModel<Character>> updateCharacterAbilityByName(@PathVariable String name, @RequestBody Character character) {
        // Get the character that needs to be updated
        Character characterToUpdate = characterRepository.findById(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found"));

        // Update the character object with the changes
        characterToUpdate.setAbility(character.getAbility());

        // Save it to the Database
        Character updatedCharacter = characterRepository.save(characterToUpdate);

        // Create an EntityModel with HATEOAS links
        EntityModel<Character> characterModel = EntityModel.of(updatedCharacter,
                linkTo(methodOn(this.getClass()).updateCharacterAbilityByName(name, character)).withSelfRel(),
                linkTo(methodOn(this.getClass()).getAllCharacters()).withRel("allCharacters")
        );

        return ResponseEntity.ok(characterModel);
    }

}
