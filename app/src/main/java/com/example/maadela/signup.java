package com.example.maadela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    EditText editName, editC0_Number, editNIC, editEmail, editPassword, editCo_Password;
    //    Button click;
    EditText txtName, txtContactNo, txtNic, txtEmail, txtPassword, txtCopassword;
    Button singupbtn, a;
    DatabaseReference dbref;
    User us;

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btn = (Button) findViewById(R.id.button);


        txtName = findViewById(R.id.editText1);
        txtContactNo = findViewById(R.id.editText2);
        // txtNic=findViewById(R.id.editText22);
        // txtEmail=findViewById(R.id.editText23);
        txtPassword = findViewById(R.id.editText3);
        txtCopassword = findViewById(R.id.editText5);

        us = new User();

    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dbref = FirebaseDatabase.getInstance().getReference().child("User");
          //  String email = txtEmail.getText().toString().trim();
            String pass = txtPassword.getText().toString().trim();
            try {
                if (TextUtils.isEmpty(txtName.getText().toString()))
                    Toast.makeText(getApplicationContext(), "enter name", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(txtContactNo.getText().toString()))
                    Toast.makeText(getApplicationContext(), "enter contact number", Toast.LENGTH_LONG).show();

                else if (TextUtils.isEmpty(txtPassword.getText().toString()))
                    Toast.makeText(getApplicationContext(), "enter password", Toast.LENGTH_SHORT).show();
                else if (!(txtPassword.getText().toString()).equals(txtCopassword.getText().toString()))
                    Toast.makeText(getApplicationContext(), "enter valid password", Toast.LENGTH_SHORT).show();
               else if (pass.length() < 6) {
                    txtPassword.setError("Password length 6 charaacters");
                    txtPassword.setFocusable(true);
                } else {
                    us.setName(txtName.getText().toString().trim());
                    us.setContactNo(Integer.parseInt(txtContactNo.getText().toString().trim()));
                    us.setPassword(txtPassword.getText().toString().trim());
                    us.setCopassword(txtCopassword.getText().toString().trim());
                    us.setStatus(true);
                    // dbref.push().setValue(us);
                    dbref.child(us.getName()).setValue(us);
                    Toast.makeText(getApplicationContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString( "username", us.getName() );
                    editor.commit();



                    Intent intent1 = new Intent(signup.this, MapLoc.class);
                    intent1.putExtra("name", us.getName());
                    startActivity(intent1);
                }


            }catch (Exception e){
                System.out.println("Error");
            }
        }



    });
    }



    public void RegCustomer(View view){

}
        }