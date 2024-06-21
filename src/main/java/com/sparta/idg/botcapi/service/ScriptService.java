package com.sparta.idg.botcapi.service;

import com.sparta.idg.botcapi.model.repositories.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScriptService {
    private final ScriptRepository scriptRepository;

    @Autowired

    public ScriptService(ScriptRepository scriptRepository) {
        this.scriptRepository = scriptRepository;
    }


}
