package com.study.totee.type;

import lombok.Getter;


@Getter
public enum SkillType {
    JavaScript("JavaScript"),
    Svelet("Svelet"),
    TypeScript("TypeScript"),
    React("React"),
    Nextjs("Nextjs"),
    Flutter("Flutter"),
    Swift("Swift"),
    Kotlin("Kotlin"),
    ReactNative("ReactNative"),
    Unity("Unity"),
    Java("Java"),
    Spring("Spring"),
    Nodejs("Nodejs"),
    Nestjs("Nestjs"),
    Go("Go"),
    Express("Express"),
    MySQL("MySQL"),
    MongoDB("MongoDB"),
    Python("Python"),
    Django("Django"),
    php("php"),
    GraphQL("GraphQL"),
    Firebase("Firebase"),
    AWS("AWS"),
    Kubernetes("Kubernetes"),
    Docker("Docker"),
    Zeplin("Zeplin"),
    Jest("Jest"),
    C("C"),
    Git("Git"),
    Figma("Figma");

    private final String skill;
    SkillType(String skill) {
        this.skill = skill;
    }
}
