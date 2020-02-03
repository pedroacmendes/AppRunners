package com.example.runners;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.runners.adapters.AtividadeAdapter;
import com.example.runners.adapters.LocalizationAdapter;
import com.example.runners.database.entity.Atividade;
import com.example.runners.viewModel.AtividadeViewModel;
import com.example.runners.viewModel.LocalizationsViewModel;
import java.util.List;

public class FragmentHistorico extends Fragment {

    private Context mContext;
    private AtividadeAdapter adapter;
    private AtividadeViewModel model;
    private LocalizationAdapter adapter2;
    private LocalizationsViewModel model2;

    public FragmentHistorico() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_historico, container, false);

        //APARECER TABELA LOCALIZATIONS
     /*   RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        adapter2 = new LocalizationAdapter(getContext());
        recyclerView.setAdapter(adapter2);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        model2 = ViewModelProviders.of(this).get(LocalizationsViewModel.class);
        model2.getAllLocalizations().observe(this, new Observer<List<Localizations>>() {
            @Override
            public void onChanged(List<Localizations> localizations) {
                adapter2.setLocalizations(localizations);
            }
        });*/


        //APARECER TABELA ATIVIDADES
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        adapter = new AtividadeAdapter(getContext());
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        model = ViewModelProviders.of(this).get(AtividadeViewModel.class);
        model.getAllAtividade().observe(this, new Observer<List<Atividade>>() {
            @Override
            public void onChanged(List<Atividade> atividades) {
                adapter.setAtividades(atividades);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

}
