package com.example.restaurant_bot_project.repository;

public class GeneratorId {
    private Integer id;

    public GeneratorId() {
        id = 1;
    }

    public Integer getId() {
        Integer currentId = id;
        id = id + 1;
        return currentId;

    }
}

