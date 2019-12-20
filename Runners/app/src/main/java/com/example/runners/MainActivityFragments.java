package com.example.runners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivityFragments extends AppCompatActivity implements Cliques{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragments);

        FragmentAtividade FragmentAtividade = new FragmentAtividade();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.add(R.id.contentor, FragmentAtividade);
        transaction.addToBackStack(null);

        transaction.commit();


    }

    @Override
    public void mudarFrag() {

        ListarFragment ListarFragment = new ListarFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.contentor, ListarFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }
}

