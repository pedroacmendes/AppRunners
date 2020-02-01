package com.example.runners.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runners.FragmentDetalhes;
import com.example.runners.R;
import com.example.runners.database.entity.Atividade;
import com.example.runners.database.entity.Localizations;

import java.util.List;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.AtividadeHolder> {

    private List<Atividade> atividades;
    private Context mContext;
    private boolean isSmatphone;

    public AtividadeAdapter(Context context) {
        this.mContext = context;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AtividadeAdapter.AtividadeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.fragment_detalhe_adapter, parent, false);

        if (v.findViewById(R.id.container) != null) {
            isSmatphone = true;
        } else {
            isSmatphone = false;
        }
        return new AtividadeHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadeAdapter.AtividadeHolder holder, int position) {
        if (atividades != null) {
            final Atividade atividade = atividades.get(position);

            holder.time.setText("" + atividade.getTime());
            holder.data.setText("" + atividade.getData());

            holder.abrir_atividade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putInt("idAtividade", atividade.getId());
                    bundle.putInt("speedAtividade", atividade.getSpeed());
                    bundle.putString("timeAtividade", atividade.getTime());
                    bundle.putString("dataAtividade", atividade.getData());
                    bundle.putDouble("altitudeAtividade", atividade.getAltitude());
                    bundle.putInt("passosAtividade", atividade.getPassos());
                    bundle.putInt("caloriasAtividade", atividade.getCalorias());
                    bundle.putString("horaInicioAtividade", atividade.getHoraInicio());
                    bundle.putString("horaFimAtividade", atividade.getHoraFim());
                    bundle.putString("temperaturaAtividade", atividade.getTemperatura());

                    FragmentDetalhes fragmentDetalhes = new FragmentDetalhes();
                    fragmentDetalhes.setArguments(bundle);

                    //if(isSmatphone){
                        transaction.replace(R.id.container, fragmentDetalhes);
                    /*} else {
                        transaction.replace(R.id.fragment2, fragmentDetalhes);
                    }*/

                    transaction.commit();

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (atividades != null)
            return atividades.size();
        return 0;
    }

    class AtividadeHolder extends RecyclerView.ViewHolder {

        public TextView time;
        public TextView data;
        public Button abrir_atividade;

        public AtividadeHolder(@NonNull View itemView) {
            super(itemView);
            abrir_atividade = itemView.findViewById(R.id.abrir_atividade);
            time = itemView.findViewById(R.id.time);
            data = itemView.findViewById(R.id.data);
        }
    }

}