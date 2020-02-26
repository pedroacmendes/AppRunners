package com.example.runners;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.runners.database.entity.Atividade;
import com.example.runners.viewModel.AtividadeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FragmentPerfil extends Fragment {

    private TextView txt_email;
    private TextView txt_id;
    private Button btn_editar;
    private Button btn_delete;
    private Button btn_sair;
    Context mContext;


    private FirebaseAuth auth;
    private FirebaseUser user;

    private AtividadeViewModel atividadeViewModel;
    LiveData<List<Atividade>> Atividades;
    Cliques cliques;

    public FragmentPerfil() {
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        mContext = getActivity();
        super.onAttachFragment(childFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        txt_email = view.findViewById(R.id.txt_email);
        txt_id = view.findViewById(R.id.txt_id);
        btn_editar = view.findViewById(R.id.btn_editar);
        btn_delete = view.findViewById(R.id.btn_delete);
        btn_sair = view.findViewById(R.id.btn_sair);

        auth = Conection.getFirebaseAuth();
        user = Conection.getFirebaseUser();
        verificaUser();

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("Eliminar");
                alert.setMessage("Tem a certeza que pretende editar o seu perfil?");
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
                alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("Eliminar");
                alert.setMessage("Tem a certeza que pretende eliminar todas as atividades?");
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
                alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        btn_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conection.logOut();
                cliques.mudarAtividade();
            }
        });

        return view;
    }

    private void verificaUser() {
        if (user == null) {
            cliques.mudarAtividade();
        } else {
            txt_email.setText("Email: " + user.getEmail());
            txt_id.setText("ID: " + user.getUid());
        }
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

