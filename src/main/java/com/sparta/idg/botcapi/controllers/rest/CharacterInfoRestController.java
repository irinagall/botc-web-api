package com.sparta.idg.botcapi.controllers.rest;


import com.sparta.idg.botcapi.model.entities.CharacterInfo;
import com.sparta.idg.botcapi.model.repositories.CharacterInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/rest")
@RestController
public class CharacterInfoRestController {
    private CharacterInfoRepository characterInfoRepository;

    @Autowired
    public CharacterInfoRestController(CharacterInfoRepository characterInfoRepository){
        this.characterInfoRepository = characterInfoRepository;
    }

    @GetMapping("/roleBasedInfo")
    public List<CharacterInfo> getAllInfo(){return characterInfoRepository.findAll();}

    @GetMapping("roleBasedInfo/id{id}")
    public  CharacterInfo getRoleBasedInfoByInfoPieceId(@PathVariable Integer id){
        return  characterInfoRepository.findCharacterInfoById(id);
    }

    @PostMapping("roleBasedInfo/new")
    public  String addRoleBasedInfo(@RequestBody CharacterInfo characterInfo){
        characterInfoRepository.save(characterInfo);
        return "New role related info added: " + characterInfo.getInfo();
    }

    @DeleteMapping("roleBasedInfo/delete/id{id}")
    public String deleteRoleBasedInfoByInfoPieceId(@PathVariable Integer id){
        if(characterInfoRepository.existsById(id)){
            characterInfoRepository.deleteById(id);
            return "You have now deleted the role based info piece with id number: " + id;
        }else{
            return "Role based info piece with id " + id + " wasn't found and therefore could not be deleted.";
        }
    }

    //
    @PutMapping("roleBasedInfo/update/id{id}")
    public CharacterInfo updateRoleBasedInfoByInfoPieceId(@PathVariable Integer id, @RequestBody CharacterInfo characterInfo) {
        CharacterInfo characterInfoToUpdate = characterInfoRepository.findById(id).get();

        characterInfoToUpdate.setInfo(characterInfo.getInfo());

        return characterInfoRepository.save(characterInfoToUpdate);
    }
}
