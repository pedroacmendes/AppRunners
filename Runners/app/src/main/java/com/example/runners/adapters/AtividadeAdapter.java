package com.example.runners.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runners.R;
import com.example.runners.database.entity.Atividade;

import java.util.List;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.AtividadeHolder> {

    private List<Atividade> atividades;

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AtividadeAdapter.AtividadeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.atividade_item, parent, false);

        return new AtividadeHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadeAdapter.AtividadeHolder holder, int position) {

        if (atividades != null) {
            Atividade atividade = atividades.get(position);
            holder.a.setText(atividade.getSpeed());
        }

    }

    @Override
    public int getItemCount() {
        if (atividades != null)
            return atividades.size();
        return 0;
    }

    class AtividadeHolder extends RecyclerView.ViewHolder {

        TextView a;

        public AtividadeHolder(@NonNull View itemView) {
            super(itemView);
            a = itemView.findViewById(R.id.speed);
        }
    }

}
