package com.example.runners;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.runners.database.entity.Atividade;
import com.example.runners.database.entity.Localizations;
import com.example.runners.viewModel.AtividadeViewModel;
import com.example.runners.viewModel.LocalizationsViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.SENSOR_SERVICE;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class FragmentAtividade extends Fragment implements OnMapReadyCallback {

    private Cliques cliques;
    private GoogleMap mGoogleMap;
    private GoogleMap mGoogleMap2;
    private SupportMapFragment mMapFragment;
    private static final int REQUEST_FINE_LOCATION = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Geocoder mGeocoder;
    private TextView txtLocation;
    private TextView txtSpeed;
    private TextView txt_kcal;
    private TextView txtAltitude;
    private TextView txthoraStart;
    private Button btnBeginAtua;
    private Button btnterminaAtividade;
    private Chronometer ch;
    private long milliseconds;

    private LocalizationsViewModel localizationsViewModel;
    private AtividadeViewModel atividadeViewModel;

    // passos
    private int count = 0;
    private SensorManager mSensorManager;
    private Sensor mDetect;
    private TextView nPassos;
    private Context mContext;

    //sobre o tempo
    private ImageView imageView = null;
    private TextView txtTemp;
    private TextView txtCidade;
    private Bitmap bitmaps = null;
    private String BaseUrl = "http://api.openweathermap.org/";
    private String AppId = "6001a24cb76e586f7133c5e1272bc6de";
    public String lat;
    public String lon;
    private String units = "metric";

    public PolylineOptions line = new PolylineOptions()
            .color(Color.RED);

    //notification
    private static final String CHANNEL_ID = "05";
    private static final int notificationId = 10;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private MyReceiver mReceiver;

    private Atividade ultimaAtividade;

    private Atividade atividade;

    ArrayList<Double> latitudeArray = new ArrayList<>();
    ArrayList<Double> longitudeArray = new ArrayList<>();

    public FragmentAtividade() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_atividade, container, false);

        createNotificationChannel();
        createNotification();

        ch = view.findViewById(R.id.simpleChronometer);
        btnBeginAtua = view.findViewById(R.id.btnBeginAtua);
        btnBeginAtua.setEnabled(true);
        btnterminaAtividade = view.findViewById(R.id.btn_terminaAtividade);
        txtLocation = view.findViewById(R.id.txtLocation);
        txtSpeed = view.findViewById(R.id.txtSpeed);
        txt_kcal = view.findViewById(R.id.txt_kcal);
        txtAltitude = view.findViewById(R.id.txtAltitude);
        txthoraStart = view.findViewById(R.id.txthoraStart);

        mReceiver = new MyReceiver();
        getContext().registerReceiver(mReceiver, new IntentFilter("acao update"));

        mSensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        mDetect = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (mSensorManager == null) {
            Toast.makeText(getActivity(), "A aplicação não reconheceu o sensor de passos.", Toast.LENGTH_SHORT).show();
        }
        nPassos = view.findViewById(R.id.nPassos);

        milliseconds = 0;

        mFusedLocationClient = getFusedLocationProviderClient(getContext());
        txtTemp = view.findViewById(R.id.txtTemp);
        txtCidade = view.findViewById(R.id.txtCidade);
        imageView = view.findViewById(R.id.imageView);
        getLastLocationTemp();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(2000);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Toast.makeText(getActivity(), "Atualizou a localização", Toast.LENGTH_SHORT).show();

                    addMarker(location);

                    latitudeArray.add(location.getLatitude());
                    longitudeArray.add(location.getLongitude());

                    mSensorManager.registerListener(new ProxSensor(), mDetect, SensorManager.SENSOR_DELAY_FASTEST);

                    int calorias = getpassos() / 20;
                    int Speed = (int) ((location.getSpeed() * 3600 / 1000));
                    Date dataHoraAtual = new Date();
                    String horaStart = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);

                    Bundle bundle = new Bundle();
                    bundle.putDouble("gpsLatitude", location.getLatitude());
                    bundle.putDouble("gpsLongitude", location.getLongitude());
                    bundle.putInt("calorias", calorias);
                    bundle.putInt("speed", Speed);
                    bundle.putInt("passos", getpassos());
                    bundle.putDouble("altitude", location.getAltitude());
                    setArguments(bundle);

                    txtAltitude.setText(" Altitude:" + location.getAltitude());
                    txtSpeed.setText(" Velocidade: " + Speed);
                    nPassos.setText(" Passos: " + getpassos());
                    txt_kcal.setText(" Calorias: " + calorias);

                    if (txthoraStart.getText().toString().matches("")) {
                        txthoraStart.setText(horaStart);
                    }

                    localizationsViewModel = ViewModelProviders.of(getActivity()).get(LocalizationsViewModel.class);
                    atividadeViewModel = ViewModelProviders.of(getActivity()).get(AtividadeViewModel.class);

                    atividadeViewModel.getAllAtividade().observe(getActivity(), new Observer<List<Atividade>>() {
                        @Override
                        public void onChanged(List<Atividade> atividades) {
                            ultimaAtividade = atividades.get(atividades.size() - 1);
                            for (int i = 0; i < latitudeArray.size(); i++) {
                                for (int j = 0; j < longitudeArray.size(); j++) {
                                    Localizations l = new Localizations(0, ultimaAtividade.getId(), latitudeArray.get(0), longitudeArray.get(0));
                                    localizationsViewModel.insere(l);
                                    LatLng latLng = new LatLng(latitudeArray.get(0), longitudeArray.get(0));
                                    line.add(latLng);
                                    longitudeArray.remove(0);
                                    latitudeArray.remove(0);
                                }
                            }
                        }
                    });
                    Geocoder(location);
                    ArrayList<Double> latitudeArray = null;
                    ArrayList<Double> longitudeArray = null;
                }
            }
        };

        btnBeginAtua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationUpdates();
                createNotification();
                mostrarNotificacao();
                atividadeViewModel = ViewModelProviders.of(getActivity()).get(AtividadeViewModel.class);

                String horaInicio = txthoraStart.getText().toString();
                String temperatura = txtTemp.getText().toString();

                Date dataHoraAtual = new Date();
                String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);

                atividade = new Atividade(0, 0, "", data, 0, 0, 0, horaInicio, "", temperatura);
                atividadeViewModel.insere(atividade);

                ch.setBase(SystemClock.elapsedRealtime());
                mSensorManager.registerListener(new ProxSensor(), mDetect, SensorManager.SENSOR_DELAY_FASTEST);
                ch.start();
            }
        });

        btnterminaAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
                cancelNotificacao();
                int calorias = getArguments().getInt("calorias");
                int speed = getArguments().getInt("speed");
                int passos = getArguments().getInt("passos");
                double altitude = getArguments().getDouble("altitude");

                String horaInicio = txthoraStart.getText().toString();
                String temperatura = txtTemp.getText().toString();

                Date dataHoraAtual = new Date();
                String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
                String horaFim = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);

                milliseconds = SystemClock.elapsedRealtime() - ch.getBase();
                int seconds = (int) (milliseconds / 1000) % 60;
                int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
                String time = hours + "h " + minutes + "m " + seconds + "s";
                ch.stop();

                atividadeViewModel = ViewModelProviders.of(getActivity()).get(AtividadeViewModel.class);

                atividade = new Atividade(ultimaAtividade.getId(), speed, time, data, altitude, passos, calorias, horaInicio, horaFim, temperatura);
                atividadeViewModel.update(atividade);

                ch.setBase(SystemClock.elapsedRealtime());

                Toast.makeText(getActivity(), "Terminou atividade!", Toast.LENGTH_SHORT).show();


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
                            txtLocation.setText(" GPS: " + location.getLatitude() + " , " + location.getLongitude());
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
        mFusedLocationClient = null;
        mLocationCallback = null;
    }

    public void Geocoder(Location location) {
        mGeocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> lista = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            txtLocation.setText(lista.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        mGoogleMap2 = map;
    }

    public void addMarker(Location location) {
        mGoogleMap.clear();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.addPolyline(line);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        mGoogleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(10)
                .strokeColor(Color.argb(50,0,0,10))
                .fillColor(Color.argb(50,0,0,10)));
        mGoogleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(2)
                .strokeColor(Color.argb(50,0,0,40))
                .fillColor(Color.argb(50,0,0,40)));
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


    //TUDO SOBRE A TEMPERATURA
    private void getLastLocationTemp() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
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

    //PASSOS
    public class ProxSensor implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            count++;
            nPassos.setText("" + count);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    public int getpassos() {
        return count;
    }

    //NOTIFICAÇAO
    private void createNotification() {

        Intent botao = new Intent("acao update");
        PendingIntent botaoPendingIntent = PendingIntent.getBroadcast(getContext(), 0, botao, PendingIntent.FLAG_ONE_SHOT);

        builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.icon_app)
                .setContentTitle("Atividade iniciada!")
                .setContentText("Passos: " + getpassos())
                .setUsesChronometer(true)
                .addAction(R.mipmap.icon_app, "Mostrar mais", botaoPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent resultIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void mostrarNotificacao() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(notificationId, builder.build());
    }

    public void alterarNotificao() {

        NotificationCompat.BigPictureStyle bpStyle = new NotificationCompat.BigPictureStyle();
        bpStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app)).build();
        builder.setStyle(bpStyle);
        mostrarNotificacao();
    }

    public void cancelNotificacao() {
        NotificationManager mNotifyMgr = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(notificationId);
    }

    private class MyReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            alterarNotificao();
        }
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

}