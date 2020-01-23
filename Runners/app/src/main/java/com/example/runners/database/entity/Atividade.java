package com.example.runners.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "atividade")
public class Atividade {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private int gps;
    private int speed;
    private String time;
    private String data;

    public Atividade(int id, int speed, int gps, String time, String data) {
        this.id = id;
        this.speed = speed;
        this.gps = gps;
        this.time = time;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getGps() {
        return gps;
    }

    public void setId(int gps) {
        this.gps = gps;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }




}
