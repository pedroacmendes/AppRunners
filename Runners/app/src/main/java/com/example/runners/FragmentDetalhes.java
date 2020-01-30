package com.example.runners;

import android.Manifest;
import android.content.Context;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class FragmentDetalhes extends Fragment implements OnMapReadyCallback {

    TextView txt_speed;
    TextView txt_time;
    TextView txt_subTitulo;
    TextView txt_titulo;
    TextView txt_altitude;
    TextView txt_passos;
    TextView txt_calorias;
    TextView txt_distancia;

    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;

    public PolylineOptions line = new PolylineOptions().color(Color.RED);

    Context mContext;

    LiveData<List<Localizations>> Locais;
    List<Localizations> coordenadas;

    LiveData<List<Atividade>> Ativi;
    List<Atividade> ativida;

    LocalizationsViewModel localizationsViewModel;
    AtividadeViewModel atividadeViewModel;

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


        if (getArguments() == null) {

            txt_titulo.setText("Sem caminhadas ralizadas :(");
            //txt_titulo.setText("Detalhes de caminhada: ");

        } else {

            int idAtividade = getArguments().getInt("idAtividade");

            atividadeViewModel = ViewModelProviders.of(getActivity()).get(AtividadeViewModel.class);
            localizationsViewModel = ViewModelProviders.of(this).get(LocalizationsViewModel.class);

            Locais = localizationsViewModel.getLocalizationById(idAtividade);
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
                            double lat1 = lat;
                            double lon1 = lon;
                        } else if (i == coordenadas.size() - 1) {
                            addMarker(lat, lon, 1);
                            double lat2 = lat;
                            double lon2 = lon;
                            // calculaDistancia(lat1, lon1, lat2, lon2);
                        } else {
                            addMarker(lat, lon, 2);
                        }
                        LatLng latLng = new LatLng(lat, lon);
                        line.add(latLng);
                    }
                }
            });

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



        /*  txt_speed.setText("Velocidade: " + ultimaAtividade.getSpeed());
            txt_time.setText(ultimaAtividade.getTime());
            txt_data.setText("Data: " + ultimaAtividade.getData());
            txt_altitude.setText("Altitude: " + ultimaAtividade.getAltitude());
            txt_passos.setText("Passos:" + ultimaAtividade.getPassos());
            txt_calorias.setText("Calorias: " + ultimaAtividade.getCalorias());
            txt_horaInicio.setText("Hora inicio:" + ultimaAtividade.getHoraInicio());
            txt_horaFim.setText("Hora fim: " + ultimaAtividade.getHoraFim());
            txt_temperatura.setText("Temperatura que estava: " + ultimaAtividade.getTemperatura());*/

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

    private double calculaDistancia(double lat1, double long1, double lat2, double long2) {

        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(long2 - long1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist * 1000; //em metros
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
