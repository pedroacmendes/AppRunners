package com.example.runners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Atividade extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mMapFragment;
    private GoogleMap mGoogleMap;

    private static final int REQUEST_FINE_LOCATION = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Geocoder mGeocoder;

    Button btnBeginAtua;
    Button btnterminaAtividade;
    TextView txtLocation;
    TextView txtGeode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        btnterminaAtividade = findViewById(R.id.btn_terminaAtividade);
        txtLocation = findViewById(R.id.txtLocation);
        txtGeode = findViewById(R.id.txtGeode);

        btnBeginAtua = findViewById(R.id.btnBeginAtua);
        btnBeginAtua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationUpdates();
            }
        });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Toast.makeText(Atividade.this, "Atualizou a localização", Toast.LENGTH_SHORT).show();
                    addMarker(location);
                    txtLocation.setText(" Latitude: " + location.getLatitude() + "\n Longitude: " +  location.getLongitude() + "\n Altitude: " + location.getAltitude() +  "\n Velocidade: " + (location.getSpeed()*3600)/1000);
                    Geocoder(location);
                }
            }
        };
        btnterminaAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
                Toast.makeText(Atividade.this, "Parou a atividade", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Atividade.this, AtividadeTerminada.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                            addMarker(location);
                            txtLocation.setText(" Latitude: " + location.getLatitude() + "\n Longitude: " +  location.getLongitude()  + "\n Altitude: " + location.getAltitude() + "\n Velocidade: " + (location.getSpeed()*3600)/1000);
                            Geocoder(location);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Errou ao obter localização!", Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }

    private void startLocationUpdates() {
        //verificar pemissoes
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null);
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


    public void Geocoder(Location location){
        mGeocoder = new Geocoder (Atividade.this, Locale.getDefault());
        try {
            List<Address> lista = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            //txtGeode.setText(lista.get(0).getAddressLine(0)); //ESCREVE A MORADA NUMA TEXTVIEW
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        //LatLng latLng = new LatLng(41.3662, -8.19928);
        /*map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("ESTG")
                .snippet("Escola Superior de Tecnologia e Gestão"));
*/
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    public void addMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

}
