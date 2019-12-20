package com.example.runners.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.runners.database.dao.AtividadeDAO;
import com.example.runners.database.entity.Atividade;

@Database(entities = {Atividade.class}, version = 1, exportSchema = false)
public abstract class AtividadeDatabase extends RoomDatabase {

    private static AtividadeDatabase INSTANCE;

    public abstract AtividadeDAO atividadeDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public static  AtividadeDatabase getInstance(final Context context){
        if(INSTANCE == null){
            synchronized (AtividadeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AtividadeDatabase.class,
                            "atividade.db"
                    )
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
