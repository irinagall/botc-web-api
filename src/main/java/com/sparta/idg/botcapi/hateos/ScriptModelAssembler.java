package com.sparta.idg.botcapi.hateos;

import com.sparta.idg.botcapi.controllers.rest.ScriptRestController;
import com.sparta.idg.botcapi.model.entities.Script;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ScriptModelAssembler implements RepresentationModelAssembler<Script, EntityModel<Script>> {

    @Override
    @NonNull
    public EntityModel<Script> toModel(@NonNull Script script) {
        return EntityModel.of(script,
                linkTo(methodOn(ScriptRestController.class).getScriptById(script.getId())).withSelfRel(),
                linkTo(methodOn(ScriptRestController.class).getAllScripts()).withRel("scripts"));
    }
}
