package com.example.maadela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity {

    String username;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_main );

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        System.out.println( "faddfafga===="+username );

        Handler handler = new Handler(  );
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {

                if( !username.equals( "" )  ){
                    DatabaseReference readref = FirebaseDatabase.getInstance().getReference().child( "User" );
                    readref.addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild( username )){

                                if((dataSnapshot.child( username ).child( "status" ).getValue().toString()).equals( "true" )){
                                   intent = new Intent( MainActivity.this,MapLoc.class );
                                    startActivity(intent);
                                    finish();
                                }else {
                                    intent = new Intent( MainActivity.this, Login.class );
                                    startActivity( intent );
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );
                }else {
                    intent = new Intent( MainActivity.this, signup.class );
                    startActivity(intent);
                    finish();
                }

            }
        },1500 );
    }
}
