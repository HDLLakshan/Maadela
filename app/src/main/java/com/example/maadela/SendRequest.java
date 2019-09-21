package com.example.maadela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

public class SendRequest extends Activity {

    DatabaseReference dbref,dref;

    ListView listViewRequest;
    List<Requests> requestsList;
    String phoneNumber;
    String DateShopOpend,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_send_request );

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        user = sharedPreferences.getString("username", "");

        requestsList = new ArrayList<>(  );
        listViewRequest = (ListView)findViewById( R.id.rlist );

        phoneNumber="0768738018";

        dbref = FirebaseDatabase.getInstance().getReference("Request").child(DateShopOpend);
        dbref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestsList.clear();
                try {
                    for (DataSnapshot reqSnapshot : dataSnapshot.getChildren()) {
                        Requests requests = reqSnapshot.getValue( Requests.class );
                        System.out.println( requests.getCusname() );
                        if((requests.getCusname()).equals(user)) {
                            requestsList.add( requests );
                            System.out.println( requestsList.get( 0 ).getFid() + "errrrkkkkk" );
                        }

                    }
                    RequestList adapter = new RequestList( SendRequest.this, requestsList );
                    listViewRequest.setAdapter( adapter );

                    listViewRequest.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if(requestsList.get( i ).getStatus().equals( "Confirmed" ))
                                DialogboxCallshop( );
                            else if(requestsList.get( i ).getStatus().equals( "Sold" ))
                                DialogboxRateShop( i );
                            else if(requestsList.get( i ).getStatus().equals( "Pending" ))
                                DialogboxDelete( i );
                        }
                    } );

                }catch (Exception e){

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }


    public void DialogboxDelete(int i){
        final int j = i;
        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setTitle( "Shop Details" );




        builder.setPositiveButton( "Delete Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete( j );
            }
        } );
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside( true );
        alertDialog.show();
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor( Color.BLACK);
    }


    public void delete(final int d){
        DatabaseReference delrf = FirebaseDatabase.getInstance().getReference().child( "Request" ).child( DateShopOpend );

        delrf.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild( requestsList.get( d ).getReqid() )){
                 dref =  FirebaseDatabase.getInstance().getReference().child( "Request" ).child( DateShopOpend ).child( requestsList.get( d ).getReqid());
                dref.removeValue();
                    Toast.makeText(getApplicationContext(), "Delete Sucessfull",Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(getApplicationContext(), "Delete Un Sucessfull",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    public void DialogboxRateShop(int i){
        final int j = i;
        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setTitle( "Rate Shop" );




        builder.setPositiveButton( "Rate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent( SendRequest.this,RatingShop.class );
                System.out.println( requestsList.get( j ).getShopname()+"=========" );
                intent.putExtra( "sname",requestsList.get( j ).getShopname() );
                intent.putExtra( "rid",requestsList.get( j ).getReqid() );
                startActivity( intent );
            }
        } );
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside( true );
        alertDialog.show();
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor( Color.BLACK);

    }

    public void DialogboxCallshop(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setTitle( "Shop Details" );

        builder.setMessage( phoneNumber );
        builder.setPositiveButton( "Call Shop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent( Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
            }
        } ).setNegativeButton( "Get Direction ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Intent intent = new Intent( SendRequest.this, )
            }
        } );

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside( true );
        alertDialog.show();
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor( Color.BLACK);
    }
}
