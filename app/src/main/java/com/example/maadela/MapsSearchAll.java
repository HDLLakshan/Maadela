package com.example.maadela;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.maps.model.Marker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MapsSearchAll extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapActivity";
    private static final String FiLo = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String CoLo = Manifest.permission.ACCESS_COARSE_LOCATION ;
    private static final int Location_re_code = 1234;
    private Boolean mLocationG = false;
    private GoogleMap MMap;
    private FusedLocationProviderClient FLPC;
    DatabaseReference dbRef;
    DatabaseReference dbshop;
    ListView listView;
    ArrayAdapter<String> AA;
    ArrayList<String> listfish;
    Fish fish;
    String DateShopOpend;
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_searched);
        getLocationPermission();
        listfish = new ArrayList<String>();
     //   fish = new Fish();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);
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
        MMap.setOnMarkerClickListener(this);
        dbRef = FirebaseDatabase.getInstance().getReference().child("DailySelling").child( DateShopOpend );
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot fishSnapshot : dataSnapshot.getChildren()){
                    final String n = fishSnapshot.getKey();
                    System.out.println("========="+n);
                    //Toast.makeText(getApplicationContext(), dataSnapshot.child(String.valueOf(i)).child("ID").getValue().toString(), Toast.LENGTH_SHORT).show();

                    DatabaseReference rf = FirebaseDatabase.getInstance().getReference().child( "location" );
                    rf.addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot fish1Snapshot : dataSnapshot.getChildren()) {
                              LocationAll l = fish1Snapshot.getValue(LocationAll.class);

                              if(l.getName().equals( n )) {


                                  // Double lat = Double.parseDouble( fish1Snapshot.child( "1" ).child( "lan" ).getValue().toString() );
                                  /// Double lng = Double.parseDouble( fish1Snapshot.child( "1" ).child( "lon" ).getValue().toString() );

                                  Double lat = l.getLan();
                                  Double lng = l.getLon();
                                  LatLng marker = new LatLng( lat, lng );
                                  // String title = fish1Snapshot.child( String.valueOf( 1 ) ).child( "name" ).getValue().toString();
                                  String title = l.getName();

                                  MMap.addMarker( new MarkerOptions().position( marker ).title( title ) ).setTag( l.getName() );
                              }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(getApplicationContext(), "All locations are loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Toast.makeText(getApplicationContext(), marker.getTag().toString(), Toast.LENGTH_SHORT).show();
        setList(marker.getTag().toString());
        name = marker.getTag().toString();
        return false;
    }
    String[] fishs = new String[100];

    public void setList(String ID){
        try {
            listfish.clear();
        }catch (Exception e){

        }

      //  Toast.makeText(getApplicationContext(), "yep", Toast.LENGTH_SHORT).show();
        dbshop = FirebaseDatabase.getInstance().getReference().child("DailySelling").child(DateShopOpend).child(ID);

        dbshop.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot fishSnapshot : dataSnapshot.getChildren())  {
                  DailySelling  fish1 = fishSnapshot.getValue(DailySelling.class);
                   // fish.setName(dataSnapshot.child(String.valueOf(i)).child("name").getValue().toString());
                  //  fish.setPrice(Integer.parseInt(dataSnapshot.child(String.valueOf(i)).child("price").getValue().toString()));
                 listfish.add(fish1.toString());
                  //  System.out.println(listfish.get( 0 ));
                 //   Toast.makeText(getApplicationContext(),fish1.getFishname(), Toast.LENGTH_SHORT).show();
                }
                listView = findViewById(R.id.listView);
                AA = new ArrayAdapter<String>(MapsSearchAll.this,android.R.layout.simple_expandable_list_item_1, listfish);
                listView.setAdapter(AA);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });







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

    public void gotoShop(View view){
        if(name==null)
            Toast.makeText( getApplicationContext(),"Select Shop On Map",Toast.LENGTH_SHORT ).show();
        else {
            Intent intent = new Intent( this, Shop.class );
            intent.putExtra( "name", name );
            startActivity( intent );
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
