package com.sparta.idg.botcapi.controllers.rest;


import com.sparta.idg.botcapi.model.entities.RoleBasedInfo;
import com.sparta.idg.botcapi.model.repositories.RoleBasedInfoRepository;
import com.sparta.idg.botcapi.service.RoleBasedInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RoleBasedInfoRestController {
    private RoleBasedInfoRepository roleBasedInfoRepository;
    private RoleBasedInfoService roleBasedInfoService;

    @Autowired
    public RoleBasedInfoRestController(RoleBasedInfoRepository roleBasedInfoRepository, RoleBasedInfoService roleBasedInfoService){
        this.roleBasedInfoRepository = roleBasedInfoRepository;
        this.roleBasedInfoService = roleBasedInfoService;
    }

    @GetMapping("/roleBasedInfo")
    public List<RoleBasedInfo> getAllRoleBasedInfo(){return roleBasedInfoRepository.findAll();}

    @GetMapping("roleBasedInfo/{id}")
    public ResponseEntity<EntityModel<RoleBasedInfo>> getRoleBasedInfoByInfoPieceId(@PathVariable Integer id) {
        RoleBasedInfo roleBasedInfo = roleBasedInfoRepository.findCharacterInfoById(id);

        if (roleBasedInfo == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<RoleBasedInfo> resourceModel = EntityModel.of(roleBasedInfo,
                linkTo(methodOn(this.getClass()).getRoleBasedInfoByInfoPieceId(id)).withSelfRel(),
                linkTo(methodOn(this.getClass()).getAllRoleBasedInfo()).withRel("allRoleBasedInfo")
        );

        return ResponseEntity.ok(resourceModel);
    }

   @PostMapping("roleBasedInfo")
   public ResponseEntity<EntityModel<RoleBasedInfo>> addRoleBasedInfo(@RequestBody RoleBasedInfo characterInfo) {
       RoleBasedInfo savedInfo = roleBasedInfoRepository.save(characterInfo);

       EntityModel<RoleBasedInfo> entityModel = EntityModel.of(savedInfo,
               linkTo(methodOn(this.getClass()).getRoleBasedInfoByInfoPieceId(savedInfo.getId())).withSelfRel(),
               linkTo(methodOn(this.getClass()).getAllRoleBasedInfo()).withRel("allRoleBasedInfo")
       );

       return ResponseEntity.created(linkTo(methodOn(this.getClass()).getRoleBasedInfoByInfoPieceId(savedInfo.getId())).toUri())
               .body(entityModel);
   }

    @DeleteMapping("roleBasedInfo/{id}")
    public ResponseEntity<?> deleteRoleBasedInfoByInfoPieceId(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        Link allInfoLink = linkTo(methodOn(this.getClass()).getAllRoleBasedInfo()).withRel("allRoleBasedInfo");

        if (roleBasedInfoRepository.existsById(id)) {
            roleBasedInfoRepository.deleteById(id);
            response.put("message", "You have now deleted the role based info piece with id number: " + id);
            return ResponseEntity.ok(EntityModel.of(response, allInfoLink));
        } else {
            response.put("message", "Role based info piece with id " + id + " wasn't found and therefore could not be deleted.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EntityModel.of(response, allInfoLink));
        }
    }

    @PutMapping("roleBasedInfo/{id}")
    public ResponseEntity<EntityModel<RoleBasedInfo>> updateRoleBasedInfoByInfoPieceId(@PathVariable Integer id, @RequestBody RoleBasedInfo characterInfo) {
        Optional<RoleBasedInfo> optionalCharacterInfo = roleBasedInfoRepository.findById(id);
        if (optionalCharacterInfo.isPresent()) {
            RoleBasedInfo characterInfoToUpdate = optionalCharacterInfo.get();
            characterInfoToUpdate.setInfo(characterInfo.getInfo());
            RoleBasedInfo updatedCharacterInfo = roleBasedInfoRepository.save(characterInfoToUpdate);

            EntityModel<RoleBasedInfo> entityModel = EntityModel.of(updatedCharacterInfo,
                    linkTo(methodOn(this.getClass()).updateRoleBasedInfoByInfoPieceId(id, characterInfo)).withSelfRel(),
                    linkTo(methodOn(this.getClass()).getAllRoleBasedInfo()).withRel("allRoleBasedInfo")
            );

            return ResponseEntity.ok(entityModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
  @GetMapping("/roleBasedInfo/character/{name}")
  public ResponseEntity<CollectionModel<EntityModel<RoleBasedInfo>>> getRoleBasedInfoByCharacterName(@PathVariable String name) {
      List<RoleBasedInfo> roleBasedInfoList = roleBasedInfoService.getRoleBasedInfoByCharacterName(name);

      List<EntityModel<RoleBasedInfo>> roleBasedInfoModels = roleBasedInfoList.stream()
              .map(info -> EntityModel.of(info,
                      linkTo(methodOn(RoleBasedInfoRestController.class).getRoleBasedInfoByCharacterName(name)).withSelfRel(),
                      linkTo(methodOn(RoleBasedInfoRestController.class).getAllRoleBasedInfo()).withRel("allRoleBasedInfo"),
                      linkTo(methodOn(CharacterRestController.class).getAllCharacters()).withRel("allCharacters")))
              .collect(Collectors.toList());

      CollectionModel<EntityModel<RoleBasedInfo>> collectionModel = CollectionModel.of(roleBasedInfoModels,
              linkTo(methodOn(RoleBasedInfoRestController.class).getRoleBasedInfoByCharacterName(name)).withSelfRel(),
              linkTo(methodOn(RoleBasedInfoRestController.class).getAllRoleBasedInfo()).withRel("allRoleBasedInfo"),
              linkTo(methodOn(CharacterRestController.class).getAllCharacters()).withRel("allCharacters"));

      return ResponseEntity.ok(collectionModel);
  }

}
