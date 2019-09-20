package com.example.maadela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderSendNotify extends AppCompatActivity {

    DatabaseReference databaseOrdereq;
    ListView reqqlistVieworders;
    List<OrderClass> reqqolist;
    DatabaseReference reqqdbr;
    String reqqCname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_send_notify);

        databaseOrdereq = FirebaseDatabase.getInstance().getReference("OrderClass");

        reqqlistVieworders = (ListView) findViewById(R.id.orreqqlist);

        reqqolist = new ArrayList<>();
        //reqCSname="FreshFish";

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        reqqCname = sharedPreferences.getString("username", "");

        databaseOrdereq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reqqolist.clear();
                for(DataSnapshot orderreq : dataSnapshot.getChildren()){
                    // ooolist.clear();
                    OrderClass oreqqrdclass = orderreq.getValue(OrderClass.class);
                    if((oreqqrdclass.getCustomerName()).equals(reqqCname)){
                          if(oreqqrdclass.getStatus().equals("Confirmed"))
                        reqqolist.add(oreqqrdclass);}
                }
                ArrayAdapter adapter = new FishOrderRequestList(OrderSendNotify.this,reqqolist);
                //ArrayAdapter adapter = new OrderList(OrderSendNotify.this,reqqolist);
                reqqlistVieworders.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}