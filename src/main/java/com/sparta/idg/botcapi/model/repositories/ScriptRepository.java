package com.sparta.idg.botcapi.model.repositories;

import com.sparta.idg.botcapi.model.entities.Script;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Integer> {
    Script findScriptById(Integer scriptId);
}