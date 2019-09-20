package com.example.maadela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RatingShop extends AppCompatActivity {

    String shopname,rid,DateShopOpend;
    RatingBar ratingBar;
    Float rating,currentrate,newrate;
    Button button;
    Requests req;
    DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_rating_shop );
        Intent i = getIntent();
        shopname = i.getStringExtra( "sname" );
        rid = i.getStringExtra( "rid" );
        System.out.println( "Elllllll"+shopname );

        ratingBar =(RatingBar)findViewById( R.id.ratingBar );
        button =(Button)findViewById( R.id.confirm );

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateShopOpend = df.format(c);



        button.setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               rating = ratingBar.getRating();
               final DatabaseReference rref = FirebaseDatabase.getInstance().getReference().child( "location" );
               rref.addListenerForSingleValueEvent( new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.hasChild( shopname )){
                           if(dataSnapshot.child( shopname ).hasChild( "rate" )){
                               LocationAll a = dataSnapshot.child( shopname ).getValue(LocationAll.class);
                               newrate= a.getRate()+rating;
                               a.setCount( a.getCount()+1 );
                               a.setRate( newrate );
                               rref.child( shopname ).setValue( a );

                           }else {
                               DatabaseReference dbref=FirebaseDatabase.getInstance().getReference().child( "location" ).child( shopname ).child( "rate" );
                               dbref.setValue( rating );
                               DatabaseReference dbrefcnt=FirebaseDatabase.getInstance().getReference().child( "location" ).child( shopname ).child( "count" );
                               dbrefcnt.setValue( 1 );

                           }
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               } );
               updateAsRated(rid);
               Intent i = new Intent( RatingShop.this,SearchNavi.class );
               startActivity( i );
           }
       } );



    }

    public void updateAsRated(final String id){
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        DatabaseReference upref = FirebaseDatabase.getInstance().getReference().child("Request").child( DateShopOpend );
        upref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)) {
                    req = dataSnapshot.child( id ).getValue(Requests.class);
                    req.setStatus( "Rated" );
                    dbref = FirebaseDatabase.getInstance().getReference().child( "Request" ).child( DateShopOpend ).child( id );
                    dbref.setValue( req);
                    Toast.makeText( getApplicationContext(), "Your Rated is Succesfully", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }
}
