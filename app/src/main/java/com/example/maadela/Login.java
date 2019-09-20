package com.example.maadela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextWatcher;
import android.widget.CompoundButton.OnCheckedChangeListener;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity implements TextWatcher, OnCheckedChangeListener {

    Button btn, b1, a2;
    EditText e1, e2;
    DatabaseHelper db;
    DatabaseReference dbref;
    CheckBox c;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";
    User us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        db = new DatabaseHelper(this);
        e1 = findViewById(R.id.editText17);
        e2 = findViewById(R.id.editText18);
        c = (CheckBox) findViewById(R.id.checkBox);
        if (sharedPreferences.getBoolean(KEY_REMEMBER, false))
            c.setChecked(true);
        else
            c.setChecked(false);
        e1.setText(sharedPreferences.getString(KEY_USERNAME, ""));
        e2.setText(sharedPreferences.getString(KEY_PASS, ""));
        e1.addTextChangedListener(this);
        e2.addTextChangedListener(this);
        c.setOnCheckedChangeListener(this);


        b1 = findViewById(R.id.button3);
        a2 = findViewById(R.id.textView9);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = e1.getText().toString();
                final String pw = e2.getText().toString();
                dbref = FirebaseDatabase.getInstance().getReference().child("User");
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(name)) {
                            if (pw.equals(dataSnapshot.child(name).child("password").getValue().toString())) {
                                login();
                                Intent intent4 = new Intent(Login.this, MapLoc.class);
                                intent4.putExtra("name", name);
                                startActivity(intent4);
                                Toast.makeText(getApplicationContext(), "Enter details", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getApplicationContext(), "Enter valid Password", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Enter valid username", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // Toast.makeText(getApplicationContext(),"Enter valid details",Toast.LENGTH_SHORT).show();
            }
        });
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(Login.this, signup.class);
                startActivity(io);
            }
        });

    }

    public void onClick(View view) {
        Intent intent = new Intent(Login.this, signup.class);
        startActivity(intent);
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }




    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        managePrefs();
    }
    public  void afterTextChanged(Editable editable){}
    public void onCheckedChanged(CompoundButton compoundButton,boolean b){managePrefs();}

    private void managePrefs() {
        if(c.isChecked()){
            editor.putString(KEY_USERNAME,e1.getText().toString().trim());
            editor.putString(KEY_PASS,e2.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER,true);
            editor.apply();

        }
        else{
            editor.putBoolean(KEY_REMEMBER,false);
            editor.remove(KEY_PASS);
          //  editor.remove(KEY_USERNAME);
            editor.apply();
        }

    }

    public void login(){
        DatabaseReference updRef = FirebaseDatabase.getInstance().getReference().child("User");
        updRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(e1.getText().toString())) {
                    us = dataSnapshot.child(e1.getText().toString()).getValue(User.class);
                    System.out.println( "laslaslasdl"+ us.isStatus() );
                    try {

                        us.setStatus(true);
                        dbref = FirebaseDatabase.getInstance().getReference().child("User").child(e1.getText().toString());
                        dbref.setValue(us);
                        Toast.makeText(getApplicationContext(), "Log Out successfully", Toast.LENGTH_SHORT).show();

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


}



