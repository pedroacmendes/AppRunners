package com.example.runners.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.runners.database.entity.Atividade;

import java.util.List;

@Dao
public interface AtividadeDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insereAtividade(Atividade...atividades);

    @Query("SELECT * FROM atividade")
    LiveData<List<Atividade>> getAllAtividade();

}