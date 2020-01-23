package com.example.runners;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.runners.adapters.AtividadeAdapter;
import com.example.runners.database.entity.Atividade;
import com.example.runners.viewModel.AtividadeViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.SENSOR_SERVICE;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class FragmentAtividade extends Fragment implements OnMapReadyCallback, SensorEventListener {

    Cliques cliques;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;
    private static final int REQUEST_FINE_LOCATION = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Geocoder mGeocoder;
    private final int ADICIONAR_REQUEST_CODE = 1;
    private AtividadeViewModel model;
    private AtividadeAdapter adapter;
    private TextView txtLocation;
    private TextView txtSpeed;
    private Button btnBeginAtua;
    private Button btnterminaAtividade;
    Chronometer ch;
    private long milliseconds;
    private long millisecondsStop;
    private int count = 0;
    private SensorManager mSensorManager;
    private Sensor mDetect;
    TextView nPassos;
    Context context;

    //sobre o tempo
    ImageView imageView = null;
    TextView txtTemp;
    TextView txtCidade;
    Bitmap bitmaps = null;
    String BaseUrl = "http://api.openweathermap.org/";
    String AppId = "6001a24cb76e586f7133c5e1272bc6de";
    public String lat;
    public String lon;
    String units = "metric";
    private Context mContext;


    public FragmentAtividade() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_atividade, container, false);
        ch = view.findViewById(R.id.simpleChronometer);
        btnBeginAtua = view.findViewById(R.id.btnBeginAtua);
        btnterminaAtividade = view.findViewById(R.id.btn_terminaAtividade);
        txtLocation = view.findViewById(R.id.txtLocation);
        txtSpeed = view.findViewById(R.id.txtSpeed);

        mSensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        mDetect = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mSensorManager.registerListener(this, mDetect, SensorManager.SENSOR_DELAY_FASTEST);
        nPassos = view.findViewById(R.id.nPassos);

        milliseconds = 0;
        millisecondsStop = 0;

        mFusedLocationClient = getFusedLocationProviderClient(getContext());
        txtTemp = view.findViewById(R.id.txtTemp);
        txtCidade = view.findViewById(R.id.txtCidade);
        imageView = view.findViewById(R.id.imageView);
        getLastLocationTemp();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Toast.makeText(getActivity(), "Atualizou a localização", Toast.LENGTH_SHORT).show();
                    addMarker(location);
                    int Speed = (int) ((location.getSpeed() * 3600 / 1000));
                    txtLocation.setText(" GPS: " + location.getLatitude() + " , " + location.getLongitude() + "\n Altitude: " + location.getAltitude());
                    txtSpeed.setText(" Velocidade: " + Speed);
                    nPassos.setText(" Passos: " + getpassos());
                    Geocoder(location);
                }
            }
        };

        btnBeginAtua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ch.start();
                startLocationUpdates();
                nPassos.setText(" Passos: " + getpassos());
            }
        });

        btnterminaAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                stopLocationUpdates();

                int gps = txtLocation.getText().length();
                int speed = txtSpeed.getText().length();
                int seconds = (int) (milliseconds / 1000) % 60;
                int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
                String time = " Tempo: " + hours + "h" + minutes + "m" + seconds + "s";
                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                String data = formataData.format(date);
                cliques.sendMenssage(0,speed, gps, time, data);
                ch.stop();
                ch.setBase(SystemClock.elapsedRealtime());

            }
        });

        return view;
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_LONG).show();
                            addMarker(location);
                            int Speed = (int) ((location.getSpeed() * 3600 / 1000));
                            txtLocation.setText(" GPS: " + location.getLatitude() + " , " + location.getLongitude() + "\n Altitude: " + location.getAltitude());
                            txtSpeed.setText(" Velocidade: " + Speed);
                            Geocoder(location);
                        }
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Errou ao obter localização!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    public void Geocoder(Location location) {
        mGeocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> lista = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            //txtGeode.setText(lista.get(0).getAddressLine(0)); //ESCREVE A MORADA NUMA TEXTVIEW
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
    }

    public void addMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Atual localização"));

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }


    public void startChronometer(View v) {
        millisecondsStop = millisecondsStop > 0 ? System.currentTimeMillis() - millisecondsStop : 0;
        ch.setBase(SystemClock.elapsedRealtime() - (milliseconds + millisecondsStop));
        ch.start();
        millisecondsStop = 0;
    }


    public void pauseChronometer(View v) {
        millisecondsStop = System.currentTimeMillis();
        milliseconds = SystemClock.elapsedRealtime() - ch.getBase();
        ch.stop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            cliques = (Cliques) context;
        } catch (Exception e) {
            Log.e("onAttach", e.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        count++;
        nPassos.setText("" + count);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public int getpassos() {
        return count;
    }


    //TUDO SOBRE A TEMPERATURA

    private void getLastLocationTemp() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(),new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = String.valueOf(location.getLatitude());
                    lon = String.valueOf(location.getLongitude());
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BaseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    com.androidmads.openweatherapi.WeatherService service = retrofit.create(com.androidmads.openweatherapi.WeatherService.class);
                    Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, AppId, units);
                    call.enqueue(new Callback<WeatherResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                            if (response.code() == 200) {
                                WeatherResponse weatherResponse = response.body();
                                assert weatherResponse != null;

                                int tempo = (int) weatherResponse.main.temp;
                                String cidade = weatherResponse.name;
                                String ico = weatherResponse.weather.get(0).icon;
                                txtTemp.setText(tempo + "º");
                                txtCidade.setText(cidade);

                                FragmentAtividade.AsyncTaskExample asyncTask = new FragmentAtividade.AsyncTaskExample();
                                String stringImg = "http://openweathermap.org/img/w/" + ico + ".png";
                                URL url = null;
                                try {
                                    url = new URL(stringImg);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                asyncTask.execute(url);

                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                            txtTemp.setText(t.getMessage());
                        }
                    });

                }
            }
        })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });

    }

    class AsyncTaskExample extends AsyncTask<URL, Integer, Bitmap> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Bitmap doInBackground(URL... urls) {
            for (int i = 0; i < urls.length; i++) {
                try {
                    bitmaps = BitmapFactory.decodeStream(urls[i].openConnection().getInputStream());
                } catch (IOException e) {
                }
            }
            return bitmaps;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


    }


}