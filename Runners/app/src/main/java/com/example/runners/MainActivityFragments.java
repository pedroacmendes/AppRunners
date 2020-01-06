package com.example.runners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    public void mudarFrag2() {

        ItemFragment itemFragment = new ItemFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.contentor, itemFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_historico:
                Intent intent = new Intent(this, ListarFragment.class);
                startActivity(intent);
                break;
            case R.id.action_sair:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

