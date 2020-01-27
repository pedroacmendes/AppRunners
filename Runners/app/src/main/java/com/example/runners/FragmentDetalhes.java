package com.example.runners;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class FragmentDetalhes extends Fragment {

    TextView txt_id;
    TextView txt_speed;
    TextView txt_gps;
    TextView txt_time;
    TextView txt_data;




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


        txt_id = view.findViewById(R.id.txt_id);
        txt_speed = view.findViewById(R.id.txt_speed);
        txt_gps = view.findViewById(R.id.txt_gps);
        txt_time = view.findViewById(R.id.txt_time);
        txt_data = view.findViewById(R.id.txt_data);

        int idAtividade = getArguments().getInt("idAtividade");
        int speedAtividade = getArguments().getInt("speedAtividade");
        int gpsAtividade = getArguments().getInt("gpsAtividade");
        String timeAtividade = getArguments().getString("timeAtividade");
        String dataAtividade = getArguments().getString("dataAtividade");

        txt_id.setText("ID: " + idAtividade);
        txt_speed.setText("Velocidade: " + speedAtividade);
        txt_gps.setText("Gps: " + gpsAtividade);
        txt_time.setText(timeAtividade);
        txt_data.setText("Data: " + dataAtividade);

        return view;
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
