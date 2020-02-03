package com.example.runners.repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.example.runners.database.AtividadeDatabase;
import com.example.runners.database.dao.LocalizationsDAO;
import com.example.runners.database.entity.Localizations;
import java.util.List;

public class LocalizationsRepository {

    private LocalizationsDAO localizationsDAO;
    private LiveData<List<Localizations>> allLocalizations;
    private LiveData<List<Localizations>> localizationById;

    public LocalizationsRepository(Application application) {
        AtividadeDatabase db = AtividadeDatabase.getInstance(application);
        localizationsDAO = db.localizationsDAO();
        allLocalizations = localizationsDAO.getAllLocalization();
    }

    public LiveData<List<Localizations>> getAllLocalization() {
        return allLocalizations;
    }

    public void insereLocalizations(Localizations a) {
        new InsertAsync(localizationsDAO).execute(a);
    }

    public LiveData<List<Localizations>> getLocalizationById(int idAtividade) {
        return localizationById = localizationsDAO.getLocalizationById(idAtividade);
    }

    private static class InsertAsync extends AsyncTask<Localizations, Void, Void> {

        private LocalizationsDAO localizationsDAO;

        public InsertAsync(LocalizationsDAO localizationsDAO) {
            this.localizationsDAO = localizationsDAO;
        }

        @Override
        protected Void doInBackground(Localizations... localizations) {
            localizationsDAO.insereLocalizations(localizations[0]);
            return null;
        }
    }

}
