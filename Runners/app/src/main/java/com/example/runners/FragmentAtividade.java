package com.example.runners;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FragmentAtividade extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;

    private static final int REQUEST_FINE_LOCATION = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Geocoder mGeocoder;

    private TextView txtLocation;
    private TextView txtGeode;
    private Button btnBeginAtua;
    private Button btnterminaAtividade;
    private Button btnListarAtividade;

    Context context;

    public FragmentAtividade() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_atividade, container, false);

        btnBeginAtua = view.findViewById(R.id.btnBeginAtua);
        btnterminaAtividade = view.findViewById(R.id.btn_terminaAtividade);
        btnListarAtividade = view.findViewById(R.id.btn_listarAtividades);
        txtLocation = view.findViewById(R.id.txtLocation);
        txtGeode = view.findViewById(R.id.txtGeode);

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
                    txtLocation.setText(" Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude() + "\n Altitude: " + location.getAltitude() + "\n Velocidade: " + Speed);
                    Geocoder(location);
                }
            }
        };

        btnBeginAtua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationUpdates();
            }
        });

        btnterminaAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
            }
        });

        btnListarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Cliques)context).mudarFrag();
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
                            txtLocation.setText(" Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude() + "\n Altitude: " + location.getAltitude() + "\n Velocidade: " + (location.getSpeed() * 3600) / 1000);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}