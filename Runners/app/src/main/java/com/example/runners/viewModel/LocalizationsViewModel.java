package com.example.runners.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.runners.database.entity.Localizations;
import com.example.runners.repository.LocalizationsRepository;
import java.util.List;

public class LocalizationsViewModel extends AndroidViewModel {

    private LocalizationsRepository repository;
    private LiveData<List<Localizations>> allLocalizations;
    private LiveData<List<Localizations>> localizationById;

    public LocalizationsViewModel(@NonNull Application application) {
        super(application);
        repository = new LocalizationsRepository(application);
        allLocalizations = repository.getAllLocalization();
    }

    public LiveData<List<Localizations>> getAllLocalizations(){
        return allLocalizations;
    }

    public LiveData<List<Localizations>> getLocalizationById(int idAtividade){
        return localizationById = repository.getLocalizationById(idAtividade);
    }

    public void insere(Localizations localization){
        repository.insereLocalizations(localization);
    }




}
