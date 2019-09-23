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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderSendNotify extends Activity {

    DatabaseReference databaseOrdereq;
    ListView reqqlistVieworders;
    List<OrderClass> reqqolist;
    DatabaseReference reqqdbr;
    String reqqCname;
    String shopcontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_send_notify);

        databaseOrdereq = FirebaseDatabase.getInstance().getReference("OrderClass");

        reqqlistVieworders = (ListView) findViewById(R.id.orreqqlist);

        reqqolist = new ArrayList<>();


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

                reqqlistVieworders.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        getSellContact(i);
                    }
                } );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void DialogboxCallshop(int k){
        final int j = k;
        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setTitle( "Select an option " );

        //uilder.setMessage( shopcontact );
        builder.setPositiveButton( "Call Seller", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent( Intent.ACTION_DIAL, Uri.fromParts("tel", shopcontact, null)));
            }
        } ).setNegativeButton( "Get Direction ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent( OrderSendNotify.this,MapSearched.class );
                intent.putExtra( "ShopName",reqqolist.get( j ).getSellerName() );
                startActivity( intent );
            }
        } );

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside( true );
        alertDialog.show();
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor( Color.BLACK);
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor( Color.BLACK);
    }

    public void getSellContact(int i){
        final int j = i;
        DatabaseReference dred = FirebaseDatabase.getInstance().getReference().child( "SellerUser" ).child( reqqolist.get( i ).getSellerName() );
        dred.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                    shopcontact = dataSnapshot.child( "phonenum" ).getValue().toString();
                DialogboxCallshop( j );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

}