package com.example.charity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapsActivity extends AppCompatActivity implements LocationListener {

    private LocationManager lm;
    private static final int PERMS_CALL_ID = 1234;

    private MapFragment mapFragment;
    private GoogleMap googleMap;
    private String typeMap;
    private UiSettings uiSettings;
    private BitmapDescriptor FoodIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
       FragmentManager fragmentManager = getFragmentManager();
       mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        typeMap =getIntent().getStringExtra("type");


    }

    @Override
    protected void onResume() {
        super.onResume();
      checkPermissions();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(lm != null)
        {
            lm.removeUpdates(this);
        }
    }

    private void checkPermissions()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },PERMS_CALL_ID);
            return;
        }
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
        if(lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
        {
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,10000,0,this);
        }
        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,0,this);
        }
        loadMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if( requestCode == PERMS_CALL_ID)
        {
            checkPermissions();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void loadMap()
    {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsActivity.this.googleMap = googleMap;
                //googleMap.clear();
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker m) {
                        String name = m.getTitle();
                        moveToAssociation(name);

                        return true;
                    }
                });
                googleMap.moveCamera(CameraUpdateFactory.zoomTo( 15 ));
                googleMap.setMyLocationEnabled(true);
                uiSettings = googleMap.getUiSettings();
                uiSettings.setZoomControlsEnabled(true);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("associations")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot ds : task.getResult()) {
                                        Map<String,Object> association = new HashMap<>();
                                        String name = "e",type;
                                        double lattitude = 0,longitude = 0 ;
                                        association = ds.getData();

                                        type = (String) association.get("type");
                                        if(typeMap.equals("All"))
                                        {

                                            lattitude = (double) association.get("lattitude");
                                            longitude = (double) association.get("longitude");
                                            name =(String) association.get("name");

                                             googleMap.addMarker( new MarkerOptions().position( new LatLng(lattitude,longitude)).title(name)
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                                        }
                                        else
                                        {
                                            if(type.equals(typeMap))
                                            {
                                                lattitude = (double) association.get("lattitude");
                                                longitude = (double) association.get("longitude");
                                                name =(String) association.get("name");

                                               googleMap.addMarker( new MarkerOptions().position( new LatLng(lattitude,longitude)).title(name)
                                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                            }
                                        }



                                    }
                                } else {

                                }
                            }
                        });


            }
        });
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lattitude = location.getLatitude();
        double longitude = location.getLongitude();
      //  Toast.makeText(this, "Location : "+ lattitude +"/"+ longitude,Toast.LENGTH_SHORT).show();
        if(googleMap != null)
        {
            LatLng googleLocation = new LatLng(lattitude,longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
        }
    }

    private void moveToAssociation(String markername) {
        Intent intent = new Intent(MapsActivity.this, AssociationActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("name",markername);
        startActivity(intent);
    }


}