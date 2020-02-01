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
    private LiveData<List<Atividade>> atividade_1;

    public AtividadeViewModel(@NonNull Application application) {
        super(application);
        repository = new AtividadeRepository(application);
        allAtividade = repository.getAllAtividades();
        atividade_1 = repository.getAtividade();
    }

    public LiveData<List<Atividade>> getAllAtividade(){
        return allAtividade;
    }

    public LiveData<List<Atividade>> getAtividade(){
        return atividade_1;
    }

    public void insere(Atividade atividade){
        repository.insereAtividade(atividade);
    }

    public void update(Atividade atividade){
        repository.updateAtividade(atividade);
    }

}
