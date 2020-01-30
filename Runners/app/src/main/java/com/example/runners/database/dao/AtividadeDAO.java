package com.example.runners.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.runners.database.entity.Atividade;

import java.util.List;

@Dao
public interface AtividadeDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insereAtividade(Atividade... atividades);

    @Query("SELECT * FROM atividade")
    LiveData<List<Atividade>> getAllAtividade();

    @Query("SELECT * FROM atividade WHERE speed = 0")
    LiveData<List<Atividade>> getAtividade();

    @Query("UPDATE atividade SET speed= :speed, time = :time, data= :data, altitude=:altitude, passos=:passos,calorias=:calorias,horaInicio=:horaInicio, horaFim=:horaFim, temperatura=:temperatura WHERE id= :id")
    void update(int speed, String time, String data, long altitude, int passos, int calorias, String horaInicio, String horaFim, String temperatura, int id);
}
