package com.example.maadela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {
    EditText cont;
    TextView cname;
    String name;
    User us;
    DatabaseReference dbref;
    Button up,del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        cname = (TextView) findViewById( R.id.editText1 );
        cont = (EditText)findViewById( R.id.editText2 );
        up = (Button)findViewById( R.id.button );
        del=(Button)findViewById( R.id.button4 );



        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("username", "");


        us = new User();

        DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("User");
        readRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(name)){
                   us= dataSnapshot.child( name ).getValue(User.class);
                   System.out.println( "dsasaasddasa"+us.getName() );
                   cname.setText( dataSnapshot.child( name ).child( "name" ).getValue().toString());
                   cont.setText( dataSnapshot.child( name ).child( "contactNo" ).getValue().toString() );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        up.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference updRef = FirebaseDatabase.getInstance().getReference().child("User");
                updRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(name)) {
                            us = dataSnapshot.child(name).getValue(User.class);
                            String email=cont.getText().toString().trim();
                            try {
                                if (TextUtils.isEmpty(cont.getText().toString()))
                                    Toast.makeText(getApplicationContext(), "enter email", Toast.LENGTH_SHORT).show();



                                else{// us.setName(txtName.getText().toString().trim());
                                    us.setContactNo(Integer.parseInt(cont.getText().toString().trim()));

                                    //         us.setPassword(txtPassword.getText().toString());

                                    dbref = FirebaseDatabase.getInstance().getReference().child("User").child(name);
                                    dbref.setValue(us);
                                    Toast.makeText(getApplicationContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                                    recreate();
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(getApplicationContext(), "invalid", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(getApplicationContext(), "No sourse to update", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        } );

        del.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference delRef=FirebaseDatabase.getInstance().getReference().child("User");
                delRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(name)){
                            dbref =FirebaseDatabase.getInstance().getReference().child("User").child(name);
                            dbref.removeValue();
                            Toast.makeText(getApplicationContext(),"Data deleted successfully",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent( profile.this,signup.class );
                            startActivity( i );
                        }
                        else
                            Toast.makeText(getApplicationContext(),"No sourse deleted",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        } );



    }



}
