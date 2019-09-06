package com.example.maadela;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapsSearchAll extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final String FiLo = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String CoLo = Manifest.permission.ACCESS_COARSE_LOCATION ;
    private static final int Location_re_code = 1234;
    private Boolean mLocationG = false;
    private GoogleMap MMap;
    private FusedLocationProviderClient FLPC;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_searched);
        getLocationPermission();

        ListView ListView = findViewById(R.id.ListView);
        ListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,new String[]{"balaya","tuna","chuttuu",}));
    }

    public void onMapReady(GoogleMap gm) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "OnMapReady: map is ready");

        MMap = gm;
        MMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

        showAll();


        getDeviceLocation();
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        MMap.setMyLocationEnabled(true);
    }

    public void showAll(){
        dbRef = FirebaseDatabase.getInstance().getReference().child("location");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(int i=1;i<=dataSnapshot.getChildrenCount();i++) {
                    LatLng negombo = new LatLng(Double.parseDouble(dataSnapshot.child(String.valueOf(i)).child("lan").getValue().toString()), Double.parseDouble(dataSnapshot.child(String.valueOf(i)).child("lon").getValue().toString()));
                    MMap.addMarker(new MarkerOptions().position(negombo).title(dataSnapshot.child(String.valueOf(i)).child("name").getValue().toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Toast.makeText(getApplicationContext(), "All locations are loaded", Toast.LENGTH_SHORT).show();


    }

    public void showPlace(final String FishType){
        dbRef = FirebaseDatabase.getInstance().getReference().child("location");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(int i=1;i<=dataSnapshot.getChildrenCount();i++) {
                    if(dataSnapshot.child(String.valueOf(i)).child("name").getValue().toString().equalsIgnoreCase(FishType)) {
                        LatLng negombo = new LatLng(Double.parseDouble(dataSnapshot.child(String.valueOf(i)).child("lan").getValue().toString()), Double.parseDouble(dataSnapshot.child(String.valueOf(i)).child("lon").getValue().toString()));
                        MMap.addMarker(new MarkerOptions().position(negombo).title(dataSnapshot.child(String.valueOf(i)).child("name").getValue().toString()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Toast.makeText(getApplicationContext(), "All locations are loaded", Toast.LENGTH_SHORT).show();
    }


    private void getDeviceLocation(){
        Log.d(TAG,"device location");

        FLPC = LocationServices.getFusedLocationProviderClient(this);

        try{
            Task location = FLPC.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Log.d(TAG,"found location");
                        Location current = (Location) task.getResult();

//                        System.out.println(current.getLatitude());

                        moveCamera(new LatLng(current.getLatitude(),current.getLongitude())
                                ,15f);

                    }else{
                        Log.d(TAG,"found location: null");
                        Toast.makeText(MapsSearchAll.this,"Unable",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (SecurityException e){
            Log.e(TAG,"getDevice location"+ e.getMessage());
        }

    }

    private void moveCamera(LatLng latLan,float zoom){
        Log.d(TAG,"MOVING...");

        MMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLan,zoom));
    }

    private void initMap(){
        Log.d(TAG,"Initializing Map");
        SupportMapFragment MF=( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        MF.getMapAsync(MapsSearchAll.this);

    }

    private void getLocationPermission(){
        Log.d(TAG,"getting location");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FiLo)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),CoLo)== PackageManager.PERMISSION_GRANTED ){

                mLocationG=true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,permissions,Location_re_code);
            }
        }else{
            ActivityCompat.requestPermissions(this,permissions,Location_re_code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationG=false;

        switch(requestCode){
            case Location_re_code:{
                if(grantResults.length>0){
                    for(int i =0;i<grantResults.length;i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationG=false;
                            Log.d(TAG,"Permission failed");
                            break;
                        }
                    }
                    Log.d(TAG,"Permission Granted");
                    mLocationG=true;
                    //intiate our map
                    initMap();
                }

            }
        }
    }


    private String getURL(LatLng l1,LatLng l2){
        String str_org= "örigin"+ l1.latitude+","+l1.longitude;

        String str_dest = "destination=1"+ l2.latitude+","+l2.longitude;

        String sensor ="sensor=false";

        String mode="mode=driving";

        String param = str_org+"&"+str_dest+"&"+sensor+"&"+mode;

        String output = "json";

        String URL="https://maps.googleapis.com/maps/api/directions/"+ output+"?"+param;

        return URL;
    }

    private String requestDirection(String requrl) throws IOException {
        String respose="";
        InputStream is=null;
        HttpURLConnection uc= null;

        try{
            URL url= new URL(requrl);
            uc = (HttpURLConnection)url.openConnection();
            uc.connect();

            is = uc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            StringBuffer stringBuffer = new StringBuffer();
            String line="";
            while ((line = br.readLine()) != null){
                stringBuffer.append(line);
            }

            respose = stringBuffer.toString();
            br.close();
            isr.close();

        }catch (Exception e){

        }finally {
            if(is != null){
                is.close();
            }
            uc.disconnect();
        }
        return respose;

    }


}
