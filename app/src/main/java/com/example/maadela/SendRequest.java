package com.example.maadela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

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

    DatabaseReference dbref;

    ListView listViewRequest;
    List<Requests> requestsList;

    String DateShopOpend,shopname,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_send_request );

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);
        shopname = "FreshFish";
        user="Lakshan";
        requestsList = new ArrayList<>(  );
        listViewRequest = (ListView)findViewById( R.id.rlist );
        dbref = FirebaseDatabase.getInstance().getReference("Request").child(DateShopOpend);

        dbref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
                }catch (Exception e){
                    System.out.println( "errrrooo" );
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
