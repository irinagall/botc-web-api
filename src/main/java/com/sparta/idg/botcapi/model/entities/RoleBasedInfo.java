package com.sparta.idg.botcapi.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "character_info", schema = "StoryTeller")
public class RoleBasedInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "name")
    @JsonBackReference
    private Character character;

    @NotNull
    @Lob
    @Column(name = "info", nullable = false)
    private String info;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "CharacterInfo{" +
                "id=" + id +
               // ", character=" + character +
                ", info='" + info + '\'' +
                '}';
    }
}