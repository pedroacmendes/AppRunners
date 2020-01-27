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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.runners.adapters.AtividadeAdapter;
import com.example.runners.database.entity.Atividade;
import com.example.runners.viewModel.AtividadeViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
    TextView txt_gps;
    TextView txt_time;
    TextView txt_data;
    TextView txt_titulo;
    TextView txt_altitude;
    TextView txt_passos;
    TextView txt_calorias;
    TextView txt_horaInicio;
    TextView txt_horaFim;
    TextView txt_distancia;
    TextView txt_temperatura;

    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;
    private Polyline line;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalhes, container, false);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        txt_speed = view.findViewById(R.id.txt_speed);
        txt_gps = view.findViewById(R.id.txt_gps);
        txt_time = view.findViewById(R.id.txt_time);
        txt_data = view.findViewById(R.id.txt_data);
        txt_titulo = view.findViewById(R.id.txt_titulo);
        txt_altitude = view.findViewById(R.id.txt_altitude);
        txt_passos = view.findViewById(R.id.txt_passos);
        txt_calorias = view.findViewById(R.id.txt_calorias);
        txt_horaInicio = view.findViewById(R.id.txt_horaInicio);
        txt_horaFim = view.findViewById(R.id.txt_horaFim);
        txt_distancia = view.findViewById(R.id.txt_distancia);
        txt_temperatura = view.findViewById(R.id.txt_temperatura);

        if (getArguments() == null) {

            txt_titulo.setText("Detalhes da sua ultima atividade: ");

        } else {

            int idAtividade = getArguments().getInt("idAtividade");
            int speedAtividade = getArguments().getInt("speedAtividade");
            long gpsAtividade = getArguments().getLong("gpsAtividade");
            String timeAtividade = getArguments().getString("timeAtividade");
            String dataAtividade = getArguments().getString("dataAtividade");
            long altitudeAtividade = getArguments().getLong("altitudeAtividade");
            int passosAtividade = getArguments().getInt("passosAtividade");
            int caloriasAtividade = getArguments().getInt("caloriasAtividade");
            String horaInicioAtividade = getArguments().getString("horaInicioAtividade");
            String horaFimAtividade = getArguments().getString("horaFimAtividade");
            String temperaturaAtividade = getArguments().getString("temperaturaAtividade");

            txt_titulo.setText("Detalhes da sua corrida nº " + idAtividade);
            txt_speed.setText("Velocidade: " + speedAtividade);
            txt_gps.setText("Gps: " + gpsAtividade);
            txt_time.setText(timeAtividade);
            txt_data.setText("Data: " + dataAtividade);
            txt_altitude.setText("Altitude: " + altitudeAtividade);
            txt_passos.setText("Passos:" + passosAtividade);
            txt_calorias.setText("Calorias: " + caloriasAtividade);
            txt_horaInicio.setText("Hora inicio:" + horaInicioAtividade);
            txt_horaFim.setText("Hora fim: " + horaFimAtividade);
            txt_temperatura.setText("Temperatura que estava: " + temperaturaAtividade);


            /*LatLng latLng1 = new LatLng(41.4418, -8.29563);
            LatLng latLng2 = new LatLng(41.445903097491914, -8.300943374633789);
            mGoogleMap.addPolyline(new PolylineOptions()
                    .add(latLng1)
                    //.add(latLng2)
                    .width(8f)
                    .color(Color.RED)
                    .geodesic(true)
            );
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 16));*/
        }

        return view;

    }

    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
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
