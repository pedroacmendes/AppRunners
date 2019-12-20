package com.example.runners.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.runners.database.entity.Atividade;
import com.example.runners.repository.AtividadeRepository;

import java.util.List;

public class AtividadeViewModel extends AndroidViewModel {

    private AtividadeRepository repository;
    private LiveData<List<Atividade>> allAtividade;

    public AtividadeViewModel(@NonNull Application application) {
        super(application);
        repository = new AtividadeRepository(application);
        allAtividade = repository.getAllAtividades();
    }

    public void insere(Atividade atividade){
        repository.insereAtividade(atividade);
    }

}
