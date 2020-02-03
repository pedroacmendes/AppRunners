package com.example.runners.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.runners.database.entity.Localizations;
import java.util.List;

@Dao
public interface LocalizationsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insereLocalizations(Localizations...localizations);

    @Query("SELECT * FROM localizations")
    LiveData<List<Localizations>> getAllLocalization();

    @Query("SELECT * FROM localizations WHERE idAtividade = :idAtividade")
    LiveData<List<Localizations>> getLocalizationById(int idAtividade);


}
