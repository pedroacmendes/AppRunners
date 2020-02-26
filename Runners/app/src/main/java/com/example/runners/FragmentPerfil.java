package com.example.runners;

import android.content.Context;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentPerfil extends Fragment {

    private TextView txt_email;
    private TextView txt_id;
    private Button btn_sair;
    private FirebaseAuth auth;
    private FirebaseUser user;
    Cliques cliques;

    public FragmentPerfil() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        txt_email = view.findViewById(R.id.txt_email);
        txt_id = view.findViewById(R.id.txt_id);
        btn_sair = view.findViewById(R.id.btn_sair);

        auth = Conection.getFirebaseAuth();
        user = Conection.getFirebaseUser();
        verificaUser();

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
            txt_id.setText("ID: " + user.get());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

