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
    private LiveData<List<Atividade>> getAtividade;

    public AtividadeRepository(Application application){
        AtividadeDatabase db = AtividadeDatabase.getInstance(application);
        atividadeDAO = db.atividadeDao();
        allAtividades = atividadeDAO.getAllAtividade();
    }

    public void insereAtividade(Atividade a){
        new InsertAsync(atividadeDAO).execute(a);
    }

    public void updateAtividade(Atividade a){
        new UpdateAsync(atividadeDAO).execute(a);
    }

    public  void deleleAtividade(Atividade a){
        new DeleteAsync(atividadeDAO).execute(a);
    }

    public LiveData<List<Atividade>> getAllAtividades(){
        return allAtividades;
    }

    public LiveData<List<Atividade>> getAtividade(int idAtividade){
        return getAtividade = atividadeDAO.getAtividate(idAtividade);
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

    private static class UpdateAsync extends AsyncTask<Atividade,Void,Void> {

        private AtividadeDAO atividadeDAO;

        public UpdateAsync(AtividadeDAO atividadeDAO) {
            this.atividadeDAO = atividadeDAO;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Atividade... atividades) {

            atividadeDAO.updateAtividade(atividades[0]);
            return null;
        }
    }

    private static class DeleteAsync extends AsyncTask<Atividade,Void,Void> {
        private AtividadeDAO atividadeDAO;

        public DeleteAsync(AtividadeDAO atividadeDAO) {
            this.atividadeDAO = atividadeDAO;
        }

        @Override
        protected Void doInBackground(Atividade... atividades) {
            atividadeDAO.deleteAtividade(atividades[0]);
            return null;
        }
    }


}
