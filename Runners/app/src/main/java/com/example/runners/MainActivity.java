package com.example.runners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements Cliques {

    private Toolbar toolbar;
    TextView btn_historico;
    TextView btn_atividade;
    TextView btn_perfil;
    private boolean isSmatphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_historico = findViewById(R.id.btn_historico);
        btn_atividade = findViewById(R.id.btn_atividade);
        btn_perfil = findViewById(R.id.btn_perfil);

        if (findViewById(R.id.container) != null) {
            isSmatphone = true;
        } else {
            isSmatphone = false;
        }

        mudarFrag();

       btn_atividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarFrag();
            }
        });

        btn_historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarFrag2();
            }
        });

        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarFrag3();
            }
        });


    }

    @Override
    public void mudarFrag() {

        if (isSmatphone) {
            FragmentAtividade FragmentAtividade = new FragmentAtividade();

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction.replace(R.id.container, FragmentAtividade);
            transaction.addToBackStack(null);

            transaction.commit();
        } else {
            FragmentAtividade FragmentAtividade = new FragmentAtividade();

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction.replace(R.id.fragment, FragmentAtividade);

            transaction.addToBackStack(null);

            transaction.commit();
        }

    }

    @Override
    public void mudarFrag2() {

        if (isSmatphone) {
            FragmentHistorico FragmentHistorico = new FragmentHistorico();

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction.replace(R.id.container, FragmentHistorico);
            transaction.addToBackStack(null);

            transaction.commit();
        } else {
            FragmentHistorico FragmentHistorico = new FragmentHistorico();
            FragmentDetalhes fragmentDetalhes = new FragmentDetalhes();

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction.replace(R.id.fragment1, FragmentHistorico);
            transaction.replace(R.id.fragment2, fragmentDetalhes);

            transaction.addToBackStack(null);

            transaction.commit();
        }
    }

    @Override
    public void mudarFrag3() {

        if (isSmatphone) {
            FragmentPerfil FragmentPerfil = new FragmentPerfil();

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction.replace(R.id.container, FragmentPerfil);
            transaction.addToBackStack(null);

            transaction.commit();
        } else {
            FragmentHistorico FragmentHistorico = new FragmentHistorico();
            FragmentPerfil FragmentPerfil = new FragmentPerfil();

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction.replace(R.id.fragment1, FragmentHistorico);
            transaction.replace(R.id.fragment2, FragmentPerfil);
            transaction.addToBackStack(null);

            transaction.commit();
        }
    }

    @Override
    public void mudarAtividade() {

        Intent intent = new Intent(MainActivity.this ,LoginActivity.class);
        startActivity(intent);

    }



}

