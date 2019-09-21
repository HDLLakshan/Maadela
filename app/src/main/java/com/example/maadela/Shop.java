package com.example.maadela;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Shop extends Activity {

    ListView listviewfish;
    DatabaseReference databaseFish;
    List<DailySelling> fishlist;
    private String cusname,shopname;
    private String DateShopOpend,time;
    EditText input;
    DatabaseReference dbref;
    Requests requests;
    Float rate,count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_shop );

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        cusname = sharedPreferences.getString("username", "");

        Intent i = getIntent();
        shopname= i.getStringExtra( "name" );

        TextView head = (TextView)findViewById( R.id.header );
        head.setText( shopname );
        final TextView r = (TextView)findViewById( R.id.ratesize );


        DatabaseReference rateref = FirebaseDatabase.getInstance().getReference().child( "location" );
        rateref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child( shopname ).hasChild( "rate" )) {
                    rate = Float.parseFloat(dataSnapshot.child( shopname ).child( "rate" ).getValue().toString());
                    count = Float.parseFloat( dataSnapshot.child( shopname ).child( "count" ).getValue().toString() );
                    Float sum = rate/count;
                    DecimalFormat decimalFormat = new DecimalFormat("#.0");
                    String numberAsString = decimalFormat.format(sum);
                    r.setText( "Rating " + numberAsString );
                }
                else
                    r.setText( "Rating is 0 yet" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );







        databaseFish = FirebaseDatabase.getInstance().getReference("DailySelling").child(DateShopOpend).child(shopname) ;
        listviewfish = (ListView)findViewById( R.id.fishslist );
        fishlist = new ArrayList<>(  );

        notification();

        databaseFish.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot fishSnapshot : dataSnapshot.getChildren()){
                    DailySelling dailySelling = fishSnapshot.getValue(DailySelling.class);
                    fishlist.add( dailySelling );
                }
                FishList adapter = new FishList( Shop.this,fishlist );
                listviewfish.setAdapter( adapter );

                listviewfish.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        System.out.println("dfsfsd");
                        //Toast.makeText(getApplicationContext(),fishlist.get( i ).getFishname(),Toast.LENGTH_LONG ).show();
                        sendMessage(i);
                    }
                } );
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void sendMessage(int i) {
        final int j = i;

        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setTitle( "Request For Reserved For 1 Hour" );
        builder.setMessage( "Fish Name :"+fishlist.get( i ).getFishname()+" \n " +"Rate :"+ fishlist.get( i ).getRate());
        builder.setCancelable( false );
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint("Enter Amount (Kg)" );
        input.setInputType( InputType.TYPE_CLASS_NUMBER );
        builder.setView(input);

        builder.setPositiveButton( "Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              String s = input.getText().toString().trim();
              sendrequest( j,s );
            }
        } ).setNegativeButton( "Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        } );
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside( true );
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor( Color.BLACK);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor( Color.BLACK);
    }

    public void sendrequest(int i,String d){
        System.out.println( "nusfsdjsdjgsj"+d );
        if(d.equals( "" ))
            Toast.makeText(getApplicationContext(), "Please Enter Amount Kg",Toast.LENGTH_SHORT).show();
        else {
            time = new SimpleDateFormat( "HH:mm" ).format( new Date() );

            requests = new Requests();

            requests.setFid( fishlist.get( i ).getId() );
            requests.setFishname( fishlist.get( i ).getFishname() );
            requests.setShopname( fishlist.get( i ).getShopName() );
            requests.setCusname( cusname );
            requests.setTime( time );
            requests.setAmount( d );
            requests.setStatus( "Pending" );
            dbref = FirebaseDatabase.getInstance().getReference().child( "Request" ).child( fishlist.get( i ).getDate() );


            DatabaseReference newref = dbref.push();
            String pushid = newref.getKey();
            requests.setReqid( pushid );
            newref.setValue( requests );

            Toast.makeText( getApplicationContext(), "Data Save Succesfull", Toast.LENGTH_SHORT ).show();
        }


    }


    public void gonotification(View view){
        Intent intent = new Intent(this,SendRequest.class );
        startActivity( intent );
    }

    public void notification(){
        DatabaseReference rrref = FirebaseDatabase.getInstance().getReference().child( "Request" ).child( DateShopOpend );
        rrref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot notySnapshot : dataSnapshot.getChildren()){
                    Requests r = notySnapshot.getValue(Requests.class);
                    if(r.getStatus().equals( "Sold" )&&r.getCusname().equals( cusname )){
                        NotificationManager mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder)
                                new NotificationCompat.Builder(Shop.this)
                                        .setSmallIcon(R.drawable.icon)
                                        .setContentTitle("Rate Shop")
                                        .setContentText("    "+r.getShopname());
                        Intent resultIntent = new Intent(Shop.this, RatingShop.class); //to open an activity on touch notification
                        resultIntent.putExtra( "sname",r.getShopname() );
                        resultIntent.putExtra( "rid",r.getReqid() );
                        PendingIntent resultPendingIntent = PendingIntent
                                .getActivity(Shop.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(resultPendingIntent);
                        Notification notification = mBuilder.build();
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        mNotifyManager.notify(1, notification);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }



}

