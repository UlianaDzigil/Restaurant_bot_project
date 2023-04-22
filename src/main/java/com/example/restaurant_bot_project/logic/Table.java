package com.example.restaurant_bot_project.logic;

public class Table {
    private Integer id;
    private String table_number;

    public Table(Integer id, String table_number) {
        this.id = id;
        this.table_number = table_number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTable_number() {
        return table_number;
    }

    public void setTable_number(String table_number) {
        this.table_number = table_number;
    }
}
