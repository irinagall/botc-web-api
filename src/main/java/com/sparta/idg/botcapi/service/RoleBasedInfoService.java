package com.sparta.idg.botcapi.service;

import com.sparta.idg.botcapi.model.entities.RoleBasedInfo;
import com.sparta.idg.botcapi.model.repositories.CharacterRepository;
import com.sparta.idg.botcapi.model.repositories.RoleBasedInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleBasedInfoService {
    private final RoleBasedInfoRepository roleBasedInfoRepository;
    private final CharacterRepository characterRepository;

    @Autowired
    public RoleBasedInfoService(RoleBasedInfoRepository roleBasedInfoRepository, CharacterRepository characterRepository) {
        this.roleBasedInfoRepository = roleBasedInfoRepository;
        this.characterRepository = characterRepository;
    }

    public List<RoleBasedInfo> getRoleBasedInfoByCharacterName(String nameIdentifier) {
        return roleBasedInfoRepository.findByCharacter_Name(nameIdentifier);
    }

}
