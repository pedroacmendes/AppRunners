package com.example.runners.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "localizations")
public class Localizations {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @NonNull
    private int idAtividade;
    @NonNull
    private double latitude;
    @NonNull
    private double longitude;

    public Localizations(int id, int idAtividade, double latitude, double longitude) {
        this.id = id;
        this.idAtividade = idAtividade;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(int idAtividade) {
        this.idAtividade = idAtividade;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
