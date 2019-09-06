package com.example.maadela;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    private String shopname;
    private String DateShopOpend;
    EditText input;
    DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_shop );

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);
        shopname = "FreshFish";
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

        builder.setTitle( "Delete or Update Rate" );
        builder.setMessage( "Fish Name :"+fishlist.get( i ).getFishname()+" \n " +"Rate");
        builder.setCancelable( false );
        input = new EditText( this );
        input.setPadding( 20,0,0,0  );
        builder.setView( input );
        input.setText( Double.toString( fishlist.get( i ).getRate()) );
        builder.setPositiveButton( "Delete Fish Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete( j );
                recreate();
            }
        } ).setNegativeButton( "Update Rate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                update( j, Double.parseDouble(input.getText().toString().trim()) );
                dialogInterface.dismiss();

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

    public void update(final int j, final double d){
        final String m = fishlist.get( j ).getDate();
       // System.out.println( fishlist.get( j ).getId()+"ppppppppppppppp" );
        DatabaseReference upref = FirebaseDatabase.getInstance().getReference().child("DailySelling");
        upref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(m)) {
                    try {
                        fishlist.get( j ).setRate( d );

                        dbref = FirebaseDatabase.getInstance().getReference().child("DailySelling").child(fishlist.get( j ).getDate()).child( fishlist.get( j ).getShopName()).child( fishlist.get( j ).getId() );
                        dbref.setValue(fishlist.get( j ));
                        recreate();
                        Toast.makeText(getApplicationContext(), "Update Sucessfull",Toast.LENGTH_SHORT).show();

                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Update Unsucesssfull",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void delete(final int i){
        final String m = fishlist.get( i ).getDate();
        DatabaseReference delref = FirebaseDatabase.getInstance().getReference().child("DailySelling");
        delref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(m)){
                    dbref=FirebaseDatabase.getInstance().getReference().child("DailySelling").child(fishlist.get( i ).getDate()).child(fishlist.get( i ).getShopName()).child( fishlist.get( i ).getId());
                    dbref.removeValue();
                    //clearcontrol();
                    Toast.makeText(getApplicationContext(), "DeleteSucessfull",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(), "Delete Un Sucessfull",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
