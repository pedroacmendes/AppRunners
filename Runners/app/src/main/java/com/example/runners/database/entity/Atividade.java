package com.example.runners.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "atividade")
public class Atividade {

    @PrimaryKey
    @NonNull
    private int id;
    private int speed;

    public Atividade(int speed, int id) {
        this.speed = speed;
        this.id = id;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
