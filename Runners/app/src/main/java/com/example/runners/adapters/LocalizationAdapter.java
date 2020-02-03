package com.example.runners.adapters;

import android.content.Context;
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
import com.example.runners.database.entity.Localizations;
import java.util.List;

public class LocalizationAdapter extends RecyclerView.Adapter<LocalizationAdapter.LocalizationHolder> {

    private List<Localizations> localizations;
    private Context mContext;
    private boolean isSmatphone;

    public LocalizationAdapter(Context context) {
        this.mContext = context;
    }

    public void setLocalizations(List<Localizations> localizations) {
        this.localizations = localizations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocalizationAdapter.LocalizationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.fragment_detalhe_adapter, parent, false);

        if (v.findViewById(R.id.container) != null) {
            isSmatphone = true;
        } else {
            isSmatphone = false;
        }

        return new LocalizationHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalizationAdapter.LocalizationHolder holder, int position) {
        if (localizations != null) {
            final Localizations localization = localizations.get(position);

            holder.time.setText("" + localization.getId());
            holder.data.setText("" + localization.getIdAtividade());

            holder.abrir_atividade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    FragmentDetalhes fragmentDetalhes = new FragmentDetalhes();
                    transaction.replace(R.id.container, fragmentDetalhes);

                    transaction.commit();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (localizations != null)
            return localizations.size();
        return 0;
    }

    class LocalizationHolder extends RecyclerView.ViewHolder {

        public TextView time;
        public TextView data;
        public Button abrir_atividade;

        public LocalizationHolder(@NonNull View itemView) {
            super(itemView);
            abrir_atividade = itemView.findViewById(R.id.abrir_atividade);
            time = itemView.findViewById(R.id.time);
            data = itemView.findViewById(R.id.data);
        }
    }

}