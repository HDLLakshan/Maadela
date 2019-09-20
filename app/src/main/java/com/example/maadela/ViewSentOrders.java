package com.example.maadela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewSentOrders extends AppCompatActivity {

    ListView listvieworders;
    DatabaseReference dbOrder;
    List<OrderClass> ordlist;
    private String sellname;
    DatabaseReference dRef;
    Dialog custrDialog;
    String cusname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view_sent_orders);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        cusname = sharedPreferences.getString("username", "");
        //cusname = "venuri";
        dbOrder = FirebaseDatabase.getInstance().getReference("OrderClass");
        listvieworders = findViewById(R.id.ordervsent);
        ordlist = new ArrayList<>();

        dbOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordlist.clear();
                for(DataSnapshot orSnapshot : dataSnapshot.getChildren()){

                    OrderClass ordclass = orSnapshot.getValue(OrderClass.class);
                    if((ordclass.getCustomerName()).equals(cusname)) {
                        if(ordclass.getStatus().equals("Pending"))
                            ordlist.add(ordclass);
                    }

                }

                ArrayAdapter adapter = new OrderList(ViewSentOrders.this,ordlist);
                //OrderList adapter = new OrderList(ViewSentOrders.this,ordlist);
                listvieworders.setAdapter(adapter);

                listvieworders.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        System.out.println("dfsfsd");
                        //  Toast.makeText(getApplicationContext(),fishlist.get( i ).getFishname(),Toast.LENGTH_LONG ).show();
                        sendMessage(i);

                    }
                } );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void sendMessage(int i) {
        final int j = i;

        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setTitle( "Are you sure to delete sent request ? " );
        builder.setCancelable( false );
        builder.setPositiveButton( "Delete Sent Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete( j );
                recreate();


            }
        } );
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside( true );
        alertDialog.show();
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor( Color.BLACK);
    }

    public void delete(final int i){
        final String m = ordlist.get( i ).getId();
        DatabaseReference delref = FirebaseDatabase.getInstance().getReference().child("OrderClass");
        delref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(m)){
                    dRef=FirebaseDatabase.getInstance().getReference().child("OrderClass").child(ordlist.get( i ).getId());
                    dRef.removeValue();
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


    public void gotoONotifications(View view) {
        Intent intent = new Intent(this, OrderSendNotify.class);
        startActivity(intent);
    }


}
