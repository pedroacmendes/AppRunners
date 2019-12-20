package com.example.runners.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.runners.database.AtividadeDatabase;
import com.example.runners.database.dao.AtividadeDAO;
import com.example.runners.database.entity.Atividade;

import java.util.List;

public class AtividadeRepository {

    private AtividadeDAO atividadeDAO;
    private LiveData<List<Atividade>> allAtividades;

    public AtividadeRepository(Application application){
        AtividadeDatabase db = AtividadeDatabase.getInstance(application);
        atividadeDAO = db.atividadeDao();
        allAtividades = atividadeDAO.getAllAtividade();
    }

    public void insereAtividade(Atividade a){
        new InsertAsync(atividadeDAO).execute(a);
    }

    public LiveData<List<Atividade>> getAllAtividades(){
        return allAtividades;
    }

    private static class InsertAsync extends AsyncTask<Atividade, Void, Void> {

        private AtividadeDAO atividadeDAO;

        public InsertAsync(AtividadeDAO atividadeDAO) {
            this.atividadeDAO = atividadeDAO;
        }

        @Override
        protected Void doInBackground(Atividade... atividades) {
            atividadeDAO.insereAtividade(atividades[0]);
            return null;
        }
    }

}
