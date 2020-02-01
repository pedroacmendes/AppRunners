package com.example.runners;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorLong;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.runners.adapters.AtividadeAdapter;
import com.example.runners.database.entity.Atividade;
import com.example.runners.database.entity.Localizations;
import com.example.runners.viewModel.AtividadeViewModel;
import com.example.runners.viewModel.LocalizationsViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class FragmentDetalhes extends Fragment implements OnMapReadyCallback {

    private TextView txt_speed;
    private TextView txt_time;
    private TextView txt_subTitulo;
    private TextView txt_titulo;
    private TextView txt_altitude;
    private TextView txt_passos;
    private TextView txt_calorias;
    private TextView txt_distancia;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;
    private PolylineOptions line = new PolylineOptions().color(Color.RED);
    private Context mContext;
    private LiveData<List<Localizations>> Locais;
    private List<Localizations> coordenadas;
    private Atividade ultimaAtividade;
    private LocalizationsViewModel localizationsViewModel;
    private AtividadeViewModel atividadeViewModel;

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

        atividadeViewModel = ViewModelProviders.of(getActivity()).get(AtividadeViewModel.class);
        localizationsViewModel = ViewModelProviders.of(this).get(LocalizationsViewModel.class);

        if (getArguments() == null) {

            atividadeViewModel.getAllAtividade().observe(getActivity(), new Observer<List<Atividade>>() {
                @Override
                public void onChanged(List<Atividade> atividades) {

                    if (atividades.size() == 0) {

                        txt_titulo.setText("Sem caminhadas realizadas");

                    } else {

                        ultimaAtividade = atividades.get(atividades.size() - 1);
                        txt_titulo.setText("Detalhes de caminhada: " + ultimaAtividade.getId());
                        txt_speed.setText("" + ultimaAtividade.getSpeed());
                        txt_time.setText(ultimaAtividade.getTime());
                        txt_subTitulo.setText("Dia " + ultimaAtividade.getData() + ", as " + ultimaAtividade.getHoraInicio() + " com " + ultimaAtividade.getTemperatura() + " de temperatura.");
                        txt_altitude.setText(" " + ultimaAtividade.getAltitude());
                        txt_passos.setText("" + ultimaAtividade.getPassos());
                        txt_calorias.setText("" + ultimaAtividade.getCalorias());

                        //atualiza();

                    }
                }
            });

        } else {

            atualiza();

            int idAtividade = getArguments().getInt("idAtividade");
            int speedAtividade = getArguments().getInt("speedAtividade");
            String timeAtividade = getArguments().getString("timeAtividade");
            String dataAtividade = getArguments().getString("dataAtividade");
            long altitudeAtividade = getArguments().getLong("altitudeAtividade");
            int passosAtividade = getArguments().getInt("passosAtividade");
            int caloriasAtividade = getArguments().getInt("caloriasAtividade");
            String temperaturaAtividade = getArguments().getString("temperaturaAtividade");

            txt_titulo.setText("Detalhes de caminhada: " + idAtividade);
            txt_speed.setText("" + speedAtividade);
            txt_time.setText(timeAtividade);
            txt_subTitulo.setText("Dia " + dataAtividade + ", as " + getArguments().getString("horaInicioAtividade") + " com " + temperaturaAtividade + " de temperatura.");
            txt_altitude.setText("" + altitudeAtividade);
            txt_passos.setText("" + passosAtividade);
            txt_calorias.setText("" + caloriasAtividade);

        }

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

        if (getArguments() == null) {
            int id = ultimaAtividade.getId();

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

        } else {
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
