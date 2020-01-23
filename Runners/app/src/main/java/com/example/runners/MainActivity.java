package com.example.runners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.runners.database.entity.Atividade;
import com.example.runners.viewModel.AtividadeViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends FragmentActivity implements Cliques {

    private AtividadeViewModel model;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabllayout_id);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarid);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentAtividade(), "Atividade");
        adapter.AddFragment(new FragmentHistorico(), "Histórico");
        adapter.AddFragment(new FragmentDetalhes(), "Detalhes");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        model = ViewModelProviders.of(this).get(AtividadeViewModel.class);
    }

    @Override
    public void mudarFrag() {

        FragmentHistorico FragmentHistorico = new FragmentHistorico();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.container, FragmentHistorico);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void mudarFrag2() {

        FragmentDetalhes FragmentDetalhes = new FragmentDetalhes();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.container, FragmentDetalhes);
        transaction.addToBackStack(null);

        transaction.commit();
    }

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
                Intent intent = new Intent(this, FragmentHistorico.class);
                startActivity(intent);
                break;
            case R.id.action_sair:
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendMenssage(int id,int speed, int gps, String time, String data) {

        Atividade atividade = new Atividade(id, speed, gps, time, data);
        model.insere(atividade);

    }


}

