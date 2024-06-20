package com.sparta.idg.botcapi.controllers.rest;


import com.sparta.idg.botcapi.model.entities.RoleBasedInfo;
import com.sparta.idg.botcapi.model.repositories.CharacterInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/rest")
@RestController
public class RoleBasedInfoRestController {
    private CharacterInfoRepository characterInfoRepository;

    @Autowired
    public RoleBasedInfoRestController(CharacterInfoRepository characterInfoRepository){
        this.characterInfoRepository = characterInfoRepository;
    }

    @GetMapping("/roleBasedInfo")
    public List<RoleBasedInfo> getAllInfo(){return characterInfoRepository.findAll();}

    @GetMapping("roleBasedInfo/id{id}")
    public RoleBasedInfo getRoleBasedInfoByInfoPieceId(@PathVariable Integer id){
        return  characterInfoRepository.findCharacterInfoById(id);
    }

    @PostMapping("roleBasedInfo/new")
    public  String addRoleBasedInfo(@RequestBody RoleBasedInfo characterInfo){
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

    @PutMapping("roleBasedInfo/update/id{id}")
    public ResponseEntity<RoleBasedInfo> updateRoleBasedInfoByInfoPieceId(@PathVariable Integer id, @RequestBody RoleBasedInfo characterInfo) {
        Optional<RoleBasedInfo> optionalCharacterInfo = characterInfoRepository.findById(id);
        if (optionalCharacterInfo.isPresent()) {
            RoleBasedInfo characterInfoToUpdate = optionalCharacterInfo.get();
            characterInfoToUpdate.setInfo(characterInfo.getInfo());
            RoleBasedInfo updatedCharacterInfo = characterInfoRepository.save(characterInfoToUpdate);
            return ResponseEntity.ok(updatedCharacterInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
