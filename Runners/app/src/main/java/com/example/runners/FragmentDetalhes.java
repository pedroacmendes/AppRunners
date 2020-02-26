package com.example.runners;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.runners.database.entity.Atividade;
import com.example.runners.database.entity.Localizations;
import com.example.runners.viewModel.AtividadeViewModel;
import com.example.runners.viewModel.LocalizationsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.List;

public class FragmentDetalhes extends Fragment implements OnMapReadyCallback {

    private TextView txt_speed;
    private TextView txt_time;
    private TextView txt_subTitulo;
    private TextView txt_titulo;
    private TextView txt_altitude;
    private TextView txt_passos;
    private TextView txt_calorias;
    private TextView txt_distancia;
    private Button btn_eliminar;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;
    private PolylineOptions line = new PolylineOptions().color(Color.RED);
    private LiveData<List<Localizations>> Locais;
    LiveData<List<Atividade>> Atividades;
    private List<Localizations> coordenadas;
    private Atividade ultimaAtividade;
    private LocalizationsViewModel localizationsViewModel;
    private AtividadeViewModel atividadeViewModel;
    Context mContext;

    public FragmentDetalhes() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        mContext = getActivity();
        super.onAttachFragment(childFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalhes, container, false);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        txt_speed = view.findViewById(R.id.txt_speed);
        txt_time = view.findViewById(R.id.txt_time);
        txt_titulo = view.findViewById(R.id.txt_titulo);
        txt_subTitulo = view.findViewById(R.id.txt_subTitulo);
        txt_altitude = view.findViewById(R.id.txt_altitude);
        txt_passos = view.findViewById(R.id.txt_passos);
        txt_calorias = view.findViewById(R.id.txt_calorias);
        txt_distancia = view.findViewById(R.id.txt_distancia);
        btn_eliminar = view.findViewById(R.id.btn_eliminar);

        atividadeViewModel = ViewModelProviders.of(getActivity()).get(AtividadeViewModel.class);
        localizationsViewModel = ViewModelProviders.of(this).get(LocalizationsViewModel.class);

        atualiza();

        int speedAtividade = getArguments().getInt("speedAtividade");
        String timeAtividade = getArguments().getString("timeAtividade");
        String dataAtividade = getArguments().getString("dataAtividade");
        double altitudeAtividade = getArguments().getDouble("altitudeAtividade");
        int altitude = (int) altitudeAtividade;
        int passosAtividade = getArguments().getInt("passosAtividade");
        int caloriasAtividade = getArguments().getInt("caloriasAtividade");
        String temperaturaAtividade = getArguments().getString("temperaturaAtividade");

        txt_titulo.setText("Detalhes de caminhada: ");
        txt_speed.setText("" + speedAtividade);
        txt_time.setText(timeAtividade);
        txt_subTitulo.setText("Dia " + dataAtividade + ", as " + getArguments().getString("horaInicioAtividade") + " com " + temperaturaAtividade + " de temperatura.");
        txt_altitude.setText("" + altitude);
        txt_passos.setText("" + passosAtividade);
        txt_calorias.setText("" + caloriasAtividade);

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("Eliminar");
                alert.setMessage("Tem a certeza que pretende eliminar esta atividade?");
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int idAtividade = getArguments().getInt("idAtividade");
                        atividadeViewModel = ViewModelProviders.of(getActivity()).get(AtividadeViewModel.class);

                        Atividades = atividadeViewModel.getAtividade(idAtividade);
                        Atividades.observe(getActivity(), new Observer<List<Atividade>>() {
                            @Override
                            public void onChanged(List<Atividade> atividades) {
                                Atividades.removeObserver(this);
                                Atividade a = atividades.get(0);
                                atividadeViewModel.deleteAtividade(a);

                            }
                        });
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        FragmentHistorico FragmentHistorico = new FragmentHistorico();
                        transaction.replace(R.id.container, FragmentHistorico);
                        transaction.commit();
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

        return view;
}

    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
    }

    public void addMarker(double lat, double lon, int ref) {
        LatLng latlng = new LatLng(lat, lon);
        if (ref == 0) {
            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title("Inicio")
                    .snippet("Hora de inicio: " + getArguments().getString("horaInicioAtividade")));
        } else if (ref == 1) {
            Marker marker2 = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Fim")
                    .snippet("Hora que terminou: " + getArguments().getString("horaFimAtividade")));
        }
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
        mGoogleMap.addPolyline(line);
    }

    public double calculaDistanciaEmKM(List<Localizations> lo, TextView txt) {

        int i = 0;
        float distancia = 0;
        while (i < coordenadas.size() - 2) {
            Location start = new Location("Start Point");
            start.setLatitude(coordenadas.get(i).getLatitude());
            start.setLongitude(coordenadas.get(i).getLongitude());
            Location finish = new Location("Finish Point");
            finish.setLatitude(coordenadas.get(i + 1).getLatitude());
            finish.setLongitude(coordenadas.get(i + 1).getLongitude());
            float distance = start.distanceTo(finish);
            distancia = distance + distancia;
            i++;
        }
        DecimalFormat df = new DecimalFormat("##.##");
        txt.setText(" " + df.format(distancia / 1000) + " km");
        return distancia / 1000;
    }

    public void atualiza() {

            int id = getArguments().getInt("idAtividade");

            Locais = localizationsViewModel.getLocalizationById(id);
            Locais.observe(this, new Observer<List<Localizations>>() {
                @Override
                public void onChanged(List<Localizations> localizations) {
                    coordenadas = localizations;
                    for (int i = 0; i <= coordenadas.size() - 1; i++) {
                        Localizations l = coordenadas.get(i);
                        double lat = l.getLatitude();
                        double lon = l.getLongitude();
                        if (i == 0) {
                            addMarker(lat, lon, 0);
                        } else if (i == coordenadas.size() - 1) {
                            addMarker(lat, lon, 1);
                        } else {
                            addMarker(lat, lon, 2);
                        }
                        LatLng latLng = new LatLng(lat, lon);
                        line.add(latLng);
                        calculaDistanciaEmKM(localizations, txt_distancia);
                    }
                }
            });

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
