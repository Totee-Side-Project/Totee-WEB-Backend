package com.study.totee.utils;

import com.study.totee.api.model.Post;
import com.study.totee.api.model.Skill;
import com.study.totee.type.SkillType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SkillConverter {
    public List<Skill> convertStringToSkillEntity(List<String> skills, Post post){
        List<Skill> skillList = new ArrayList<>();
        for (String skill : skills) {
            switch (skill) {
                case "JavaScript":
                    skillList.add(new Skill(SkillType.JavaScript, post));
                    break;
                case "Svelet":
                    skillList.add(new Skill(SkillType.Svelte, post));
                    break;
                case "TypeScript":
                    skillList.add(new Skill(SkillType.TypeScript, post));
                    break;
                case "React":
                    skillList.add(new Skill(SkillType.React, post));
                    break;
                case "Nextjs":
                    skillList.add(new Skill(SkillType.Nextjs, post));
                    break;
                case "Flutter":
                    skillList.add(new Skill(SkillType.Flutter, post));
                    break;
                case "Swift":
                    skillList.add(new Skill(SkillType.Swift, post));
                    break;
                case "Kotlin":
                    skillList.add(new Skill(SkillType.Kotlin, post));
                    break;
                case "ReactNative":
                    skillList.add(new Skill(SkillType.ReactNative, post));
                    break;
                case "Unity":
                    skillList.add(new Skill(SkillType.Unity, post));
                    break;
                case "Java":
                    skillList.add(new Skill(SkillType.Java, post));
                    break;
                case "Spring":
                    skillList.add(new Skill(SkillType.Spring, post));
                    break;
                case "Nodejs":
                    skillList.add(new Skill(SkillType.Nodejs, post));
                    break;
                case "Nestjs":
                    skillList.add(new Skill(SkillType.Nestjs, post));
                    break;
                case "Go":
                    skillList.add(new Skill(SkillType.Go, post));
                    break;
                case "Express":
                    skillList.add(new Skill(SkillType.Express, post));
                    break;
                case "MySQL":
                    skillList.add(new Skill(SkillType.MySQL, post));
                    break;
                case "MongoDB":
                    skillList.add(new Skill(SkillType.MongoDB, post));
                    break;
                case "Python":
                    skillList.add(new Skill(SkillType.Python, post));
                    break;
                case "Django":
                    skillList.add(new Skill(SkillType.Django, post));
                    break;
                case "php":
                    skillList.add(new Skill(SkillType.php, post));
                    break;
                case "GraphQL":
                    skillList.add(new Skill(SkillType.GraphQL, post));
                    break;
                case "Firebase":
                    skillList.add(new Skill(SkillType.Firebase, post));
                    break;
                case "AWS":
                    skillList.add(new Skill(SkillType.AWS, post));
                    break;
                case "Kubernetes":
                    skillList.add(new Skill(SkillType.Kubernetes, post));
                    break;
                case "Docker":
                    skillList.add(new Skill(SkillType.Docker, post));
                    break;
                case "Zeplin":
                    skillList.add(new Skill(SkillType.Zeplin, post));
                    break;
                case "Jest":
                    skillList.add(new Skill(SkillType.Jest, post));
                    break;
                case "C":
                    skillList.add(new Skill(SkillType.C, post));
                    break;
                case "Git":
                    skillList.add(new Skill(SkillType.Git, post));
                    break;
                case "Figma":
                    skillList.add(new Skill(SkillType.Figma, post));
                    break;
            }
        }
        return skillList;
    }

    public List<String> convertSkillEntityToString(Set<Skill> skills){
        List<String> skillList = new ArrayList<>();
        for (Skill skill : skills) {
            skillList.add(skill.getSkill().getSkill());
        }
        return skillList;
    }
}
