package com.example.maadela;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_shop );

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);
        cusname = "Laka";
        shopname="FreshFish";
        TextView head = (TextView)findViewById( R.id.header );
        head.setText( shopname );
        databaseFish = FirebaseDatabase.getInstance().getReference("DailySelling").child(DateShopOpend).child(shopname);
        listviewfish = (ListView)findViewById( R.id.fishslist );
        fishlist = new ArrayList<>(  );

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
        builder.setMessage( "Fish Name :"+fishlist.get( i ).getFishname()+" \n " +"Rate");
        builder.setCancelable( false );
        input = new EditText( this );
        input.setPadding( 20,0,0,0  );
        builder.setView( input );
        input.setText( Double.toString( fishlist.get( i ).getRate()) );
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
        nbutton.setBackgroundColor( Color.GREEN);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor( Color.RED);
    }




    public void sendrequest(int i,String d){
        time = new SimpleDateFormat("HH:mm").format(new Date());

        requests = new Requests();

        requests.setFid( fishlist.get( i ).getId() );
        requests.setFishname( fishlist.get( i ).getFishname() );
        requests.setShopname( fishlist.get( i ).getShopName() );
        requests.setTime( d );
        requests.setCusname( cusname );
        requests.setTime( time );
        requests.setAmount( "5kg" );
        requests.setStatus( "Pending" );
        dbref = FirebaseDatabase.getInstance().getReference().child("Request").child( fishlist.get( i ).getDate() );


       // dbref.push().setValue(requests);
        DatabaseReference  newref     = dbref.push();
        String pushid = newref.getKey();
        requests.setReqid( pushid );
        newref.setValue( requests );

        Toast.makeText(getApplicationContext(), "Data Save Succesfull",Toast.LENGTH_SHORT).show();


    }


    public void gonotification(View view){
        Intent intent = new Intent(this,SendRequest.class );
        startActivity( intent );
    }

}
