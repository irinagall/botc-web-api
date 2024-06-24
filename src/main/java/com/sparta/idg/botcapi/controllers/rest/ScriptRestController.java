package com.sparta.idg.botcapi.controllers.rest;

import com.sparta.idg.botcapi.hateos.ScriptModelAssembler;
import com.sparta.idg.botcapi.model.entities.Script;
import com.sparta.idg.botcapi.model.repositories.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ScriptRestController {
    private final ScriptRepository scriptRepository;
    private final ScriptModelAssembler scriptModelAssembler;

    @Autowired
    public ScriptRestController(ScriptRepository scriptRepository, ScriptModelAssembler scriptModelAssembler) {
        this.scriptRepository = scriptRepository;
        this.scriptModelAssembler = scriptModelAssembler;
    }

    @GetMapping("/scripts")
    public List<Script> getAllScripts() {
        return scriptRepository.findAll();
    }

    @GetMapping("/scripts/{id}")
    public Script getScriptById(@PathVariable Integer id) {
        return scriptRepository.findScriptById(id);
    }

   /* @GetMapping("/script/title")
    public List<Script> getScriptByTitle(@RequestParam(name = "title", required = false) String title) {
        return scriptRepository.findAll().stream().filter(script -> script.getTitle().contains(title)).toList();
    }*/

    @GetMapping("/script/title")
    public CollectionModel<EntityModel<Script>> getScriptByTitle(@RequestParam(name = "title", required = false) String title) {
        List<Script> scripts = scriptRepository.findAll().stream()
                .filter(script -> script.getTitle().contains(title))
                .toList();

        List<EntityModel<Script>> scriptModels = scripts.stream()
                .map(script -> EntityModel.of(script,
                        linkTo(methodOn(ScriptRestController.class).getScriptById(script.getId())).withSelfRel()))
               .toList();

        return CollectionModel.of(scriptModels,
                linkTo(methodOn(ScriptRestController.class).getScriptByTitle(title)).withSelfRel(),
                linkTo(methodOn(ScriptRestController.class).getAllScripts()).withRel("allScripts"));
    }

    @PostMapping("/scripts")
    public ResponseEntity<?> addScript(@RequestBody Script script) {
        Script savedScript = scriptRepository.save(script);
        EntityModel<Script> entityModel = scriptModelAssembler.toModel(savedScript);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/scripts/{id}")
    public ResponseEntity<?> deleteScriptById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        String message;
        Link allScriptsLink = linkTo(methodOn(ScriptRestController.class).getAllScripts()).withRel("allScripts");

        if (scriptRepository.existsById(id)) {
            scriptRepository.deleteById(id);
            message = "You have now deleted the script with id number: " + id;
            response.put("message", message);
            response.put("link", allScriptsLink.getHref());
            return ResponseEntity.ok(response);
        } else {
            message = "Script with id " + id + " wasn't found and therefore could not be deleted.";
            response.put("message", message);
            response.put("link", allScriptsLink.getHref());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

//    @PutMapping("/scripts/{id}")
//    public Script updateScriptById(@PathVariable Integer id, @RequestBody Script script) {
//        //Get the script that needs to be updated
//        Script scriptToUpdate = scriptRepository.findById(id).get();
//        //Update that script object with the changes
//        scriptToUpdate.setTitle(script.getTitle());
//        //Save it to the database
//        return scriptRepository.save(scriptToUpdate);
//    }

    @PutMapping("/scripts/{id}")
    public ResponseEntity<EntityModel<Script>> updateScriptById(@PathVariable Integer id, @RequestBody Script script) {
        // Get the script that needs to be updated
        Script scriptToUpdate = scriptRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Script not found"));

        // Update that script object with the changes
        scriptToUpdate.setTitle(script.getTitle());

        // Save it to the database
        Script updatedScript = scriptRepository.save(scriptToUpdate);

        // Create an EntityModel with HATEOAS links
        EntityModel<Script> scriptModel = EntityModel.of(updatedScript,
                linkTo(methodOn(ScriptRestController.class).getScriptById(updatedScript.getId())).withSelfRel(),
                linkTo(methodOn(ScriptRestController.class).getAllScripts()).withRel("allScripts"),
                linkTo(methodOn(ScriptRestController.class).updateScriptById(updatedScript.getId(), updatedScript)).withRel("update"),
                linkTo(methodOn(ScriptRestController.class).deleteScriptById(updatedScript.getId())).withRel("delete")
        );

        return ResponseEntity.ok(scriptModel);
    }

    //Hypermedia As The Engine Of State
    @GetMapping("/script/{id}")
    public EntityModel<Script> getScriptByIdWithHateos(@PathVariable Integer id) {
        return scriptRepository.findById(id).map(mappedScript -> {
            EntityModel<Script> entityModel = EntityModel.of(mappedScript);
            entityModel.add(linkTo(methodOn(ScriptRestController.class).
                    getScriptById(id)).withSelfRel());
            entityModel.add(linkTo(methodOn(ScriptRestController.class).getAllScripts()).withRel("all scripts"));

            List<Script> allScripts = scriptRepository.findAll();

            allScripts.stream().filter(script -> !script.getId().equals(id))
                    .forEach(script -> entityModel.add(linkTo(methodOn(ScriptRestController.class).getScriptByIdWithHateos(script.getId())).withRel("script " + script.getId())));


            return entityModel;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Script not found"));
    }
}
