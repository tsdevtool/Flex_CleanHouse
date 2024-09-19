package com.example.happyhomes.Customer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityMapCustomerBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MapCustomerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private androidx.appcompat.widget.SearchView mapSearchView;
    private SupportMapFragment mapFragment;
    ActivityMapCustomerBinding binding;
    private String selectedAddress = null;
    private int cusID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Chọn Vị Trí");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMapCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        mapSearchView = findViewById(R.id.mapSearch);

        mapSearchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }



        binding.btnConfirm.setOnClickListener(v -> confirmLocation());
    }

    private void searchLocation(String location) {
        Geocoder geocoder = new Geocoder(MapCustomerActivity.this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(location, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                selectedAddress = address.getAddressLine(0);
                mapSearchView.setQuery(location, false);
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(latLng).title(location));
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Vị trí chưa được chọn", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(MapCustomerActivity.this);
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setZoomGesturesEnabled(true);
        myMap.getUiSettings().setScrollGesturesEnabled(true);

        if (currentLocation != null) {
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            myMap.addMarker(new MarkerOptions().position(currentLatLng).title("Vị trí hiện tại"));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
        }

        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Geocoder geocoder = new Geocoder(MapCustomerActivity.this);
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && !addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        String location = address.getAddressLine(0);

                        mapSearchView.setQuery(location, false);

                        myMap.clear();
                        myMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mapNone) {
            myMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        } else if (id == R.id.mapNormal) {
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (id == R.id.mapSattelite) {
            myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (id == R.id.mapHybird) {
            myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if (id == R.id.mapTerrain) {
            myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmLocation() {
        if (selectedAddress != null) {
            Intent intent = new Intent(MapCustomerActivity.this, ServiceActivity.class);
            intent.putExtra("address", selectedAddress);
            cusID = getIntent().getIntExtra("CusId", -1);
            String cusName = getIntent().getStringExtra("Cusname");
            if (cusID != -1) {

                intent.putExtra("CusId", cusID);
                intent.putExtra("Cusname",cusName);
                startActivity(intent);
            } else {
                Toast.makeText(this, "CusID not found!", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Hãy chọn vị trí ở trên bản đồ", Toast.LENGTH_SHORT).show();
        }
    }
}