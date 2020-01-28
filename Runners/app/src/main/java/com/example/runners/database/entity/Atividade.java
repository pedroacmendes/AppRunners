package com.example.runners.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "atividade")
public class Atividade {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private int speed;
    private String time;
    private String data;
    private long altitude;
    private int passos;
    private int calorias;
    private String horaInicio;
    private String horaFim;
    private String temperatura;

    public Atividade(int id, int speed, String time, String data, long altitude, int passos, int calorias, String horaInicio, String horaFim, String temperatura) {
        this.id = id;
        this.speed = speed;
        this.time = time;
        this.data = data;
        this.altitude = altitude;
        this.passos = passos;
        this.calorias = calorias;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.temperatura = temperatura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getAltitude() {
        return altitude;
    }

    public void setAltitude(long altitude) {
        this.altitude = altitude;
    }

    public int getPassos() {
        return passos;
    }

    public void setPassos(int passos) {
        this.passos = passos;
    }

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }
}
