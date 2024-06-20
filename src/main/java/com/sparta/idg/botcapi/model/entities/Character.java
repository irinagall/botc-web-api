package com.sparta.idg.botcapi.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "characters", schema = "StoryTeller")
public class Character {
    @Id
    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "ability")
    private String ability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "script_id")
    @JsonBackReference
    private Script script;

    @OneToMany(mappedBy = "character")
    private Set<CharacterInfo> characterInfoAdvice = new LinkedHashSet<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public Set<CharacterInfo> getCharacterInfoAdvice() {
        return characterInfoAdvice;
    }

    public void setCharacterInfoAdvice(Set<CharacterInfo> characterInformationAdvice) {
        this.characterInfoAdvice = characterInformationAdvice;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", ability='" + ability + '\'' +
                ", script=" + script.getTitle() +
                //", characterInfos=" + characterInfoAdvice +
                '}';
    }
}