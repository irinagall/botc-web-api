package com.sparta.idg.botcapi.controllers.rest;

import com.sparta.idg.botcapi.model.entities.Script;
import com.sparta.idg.botcapi.model.repositories.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScriptRestController {
    private final ScriptRepository scriptRepository;

   @Autowired
    public ScriptRestController(ScriptRepository scriptRepository) {
        this.scriptRepository = scriptRepository;
    }

    @GetMapping("/rest/allscripts")
    public List<Script> getAllScripts() {
       return scriptRepository.findAll();
    }

    @GetMapping("/rest/script/id{id}")
    public Script getScriptById(@PathVariable Integer id){
        return scriptRepository.findScriptById(id);
    }

    

}
