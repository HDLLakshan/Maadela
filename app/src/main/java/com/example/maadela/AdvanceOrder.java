package com.example.maadela;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdvanceOrder extends AppCompatActivity {

    private Spinner spinner;

    Button ordbtn;
    EditText amount , date;
    Spinner type;
    DatabaseReference db;
    OrderClass od;
    private String customer;
    private String sellers;
    private String  status;
    private String custcontact;
    private String sellcontact;
    private double totprices;


    private void clearControls(){

        amount.setText("");
        date.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_order);

        addListenerOnSpinnerItemSelection();

        type = spinner;
        amount = findViewById(R.id.editText2);
        date = findViewById(R.id.editText3);
        ordbtn =  findViewById(R.id.orderBtn);
        od = new OrderClass();

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        customer = sharedPreferences.getString("username", "");

       custcontact = "0784567435";
       status = "Pending";

        //SharedPreferences preferences = getSharedPreferences( "customer",MODE_PRIVATE );
        //customer = preferences.getString( "username","" );

        db = FirebaseDatabase.getInstance().getReference().child("OrderClass");


        ordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(type.getSelectedItem().toString()))
                    Toast.makeText(getApplicationContext(),"Please select fish type ",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(amount.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Please enter amount ",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(date.getText().toString()))
                    Toast.makeText(getApplicationContext(),"select a date",Toast.LENGTH_SHORT).show();
                else {
                    od.setType(type.getSelectedItem().toString());
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
                    //String pushid = db.getKey();
                    //od.setId(pushid);
                    //db.push().setValue(od);


                    Toast.makeText(getApplicationContext(),"Order has been saved",Toast.LENGTH_SHORT).show();
                    clearControls();
                }
            }
        });



    }




    public void addListenerOnSpinnerItemSelection() {
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new  CustomOnItemSelectedListener());
        //ordertype = spinner;
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

    public void sendviewRMessage(View view) {
        Intent intent = new Intent(this, ViewSentOrders.class);
        startActivity(intent);
    }


}