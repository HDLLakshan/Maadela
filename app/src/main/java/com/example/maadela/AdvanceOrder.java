package com.example.maadela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdvanceOrder extends AppCompatActivity {


    Button ordbtn;
    EditText amount , date;
    DatabaseReference db;
    OrderClass od;
    private String customer;
    private String sellers;
    private String  status;
    private String custcontact;
    private String sellcontact;
    private double totprices;
    AutoCompleteTextView et;


    private void clearControls(){

        amount.setText("");
        date.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_order);

//        addListenerOnSpinnerItemSelection();


        amount = findViewById(R.id.editText2);
        date = findViewById(R.id.editText3);
        ordbtn =  findViewById(R.id.orderBtn);
        od = new OrderClass();
        et = findViewById( R.id.fishname );

        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1,FishItemNames.Fish );
        et.setAdapter( adapter );

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        customer = sharedPreferences.getString("username", "");


       status = "Pending";
       getphonenum();

        db = FirebaseDatabase.getInstance().getReference().child("OrderClass");


        ordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(et.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Please select fish type ",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(amount.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Please enter amount ",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(date.getText().toString()))
                    Toast.makeText(getApplicationContext(),"select a date",Toast.LENGTH_SHORT).show();
                else {
                    od.setType(et.getText().toString());
                    od.setAmount(Double.parseDouble(amount.getText().toString().trim()));
                    od.setDate(date.getText().toString());
                    od.setCustomerName(customer);
                    od.setCustomerContact(custcontact);
                    od.setSellerName(sellers);
                    od.setStatus(status);
                    od.setSellerContact(sellcontact);
                    od.setTotprice(totprices);


                    DatabaseReference  newref     = db.push();
                    String pushid = newref.getKey();
                    od.setId( pushid );
                    newref.setValue( od);


                    Toast.makeText(getApplicationContext(),"Order has been saved",Toast.LENGTH_SHORT).show();
                    clearControls();
                }
            }
        });



    }

    public void onStart() {

        super.onStart();
        EditText txtdates = findViewById(R.id.editText3);
        txtdates.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus){
                    DateDialog dialog;
                    dialog = new DateDialog(v);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    dialog.show(fragmentTransaction,"DatePicker");
                }
            }
        });

    }

    public void getphonenum(){
        DatabaseReference rref = FirebaseDatabase.getInstance().getReference().child( "User" ).child( customer );
        rref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                    custcontact = dataSnapshot.child( "contactNo" ).getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    public void sendviewRMessage(View view) {
        Intent intent = new Intent(this, ViewSentOrders.class);
        startActivity(intent);
    }


}