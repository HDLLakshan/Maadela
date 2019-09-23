package com.example.maadela;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class FishOrderRequestList extends ArrayAdapter<OrderClass> {


    private Activity context;
    private List<OrderClass> orderListreq;

    public FishOrderRequestList(Activity context , List<OrderClass> orderListreq){
        super(context ,R.layout.fish_orequest_list,orderListreq );
        this.context = context;
        this.orderListreq = orderListreq;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View orreqderListview = inflater.inflate(R.layout.fish_orequest_list,null,true);


        TextView orrqfishtype = orreqderListview.findViewById(R.id.orrtype);
        TextView orrqsellername = orreqderListview.findViewById(R.id.orrcname);
        TextView orrqamount = orreqderListview.findViewById(R.id.orramount);
        TextView orrqdate = orreqderListview.findViewById(R.id.orrdate);
        TextView orrqstatus = orreqderListview.findViewById(R.id.orrstatus);
        TextView orrqContact = orreqderListview.findViewById(R.id.orrContact);
        TextView orrqpricetot = orreqderListview.findViewById(R.id.orrTotprice);



        OrderClass oqrderClass = orderListreq.get(position);





        orrqfishtype.setText("  Fish Type : "+oqrderClass.getType());
        orrqsellername.setText("  Seller Name : "+oqrderClass.getSellerName());
        orrqamount.setText("  Amount : "+new Double(oqrderClass.getAmount()).toString()+ "Kg");
        orrqdate.setText("  Date Requested : "+oqrderClass.getDate());
        orrqstatus.setText("  Status : "+oqrderClass.getStatus());
        orrqContact.setText("  Seller Contact : "+oqrderClass.getSellerContact());
        orrqpricetot.setText("  Total Price : "+oqrderClass.getTotprice());



        return orreqderListview;
    }


}
