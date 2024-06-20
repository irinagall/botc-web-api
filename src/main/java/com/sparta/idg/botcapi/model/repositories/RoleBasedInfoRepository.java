package com.sparta.idg.botcapi.model.repositories;

import com.sparta.idg.botcapi.model.entities.RoleBasedInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleBasedInfoRepository extends JpaRepository<RoleBasedInfo, Integer> {
    RoleBasedInfo findCharacterInfoById(Integer id);
    
}