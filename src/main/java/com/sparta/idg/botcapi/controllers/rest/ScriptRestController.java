package com.sparta.idg.botcapi.controllers.rest;

import com.sparta.idg.botcapi.model.entities.Script;
import com.sparta.idg.botcapi.model.repositories.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/rest")
@RestController
public class ScriptRestController {
    private final ScriptRepository scriptRepository;

   @Autowired
    public ScriptRestController(ScriptRepository scriptRepository) {
        this.scriptRepository = scriptRepository;
    }

    @GetMapping("/scripts")
    public List<Script> getAllScripts() {
       return scriptRepository.findAll();
    }

    @GetMapping("/script/id{id}")
    public Script getScriptById(@PathVariable Integer id){
        return scriptRepository.findScriptById(id);
    }

    @GetMapping("/script/title")
    public List<Script> getScriptByTitle(@RequestParam(name = "title", required = false) String title){
       return scriptRepository.findAll().stream().filter(script -> script.getTitle().contains(title)).toList();
    }

    @PostMapping("/script/new")
    public String addScript(@RequestBody Script script){
       scriptRepository.save(script);
       return  "New script added: " + script.getTitle();
    }

    @DeleteMapping("/script/delete/id{id}")
    public String deleteScriptById(@PathVariable Integer id){
       if(scriptRepository.existsById(id)){
           scriptRepository.deleteById(id);
           return "You have now deleted the script with id number: " + id;
       }else{
           return "Script with id "+ id + " wasn't found and therefore could not be deleted.";
       }
    }

    @PutMapping("/script/update/id{id}")
    public Script updateScriptById(@PathVariable Integer id, @RequestBody Script script){
       //Get the script that needs to be updated
        Script scriptToUpdate = scriptRepository.findById(id).get();
        //Update that script object with the changes
        scriptToUpdate.setTitle(script.getTitle());
        //Save it to the database
        return scriptRepository.save(scriptToUpdate);
    }

}
