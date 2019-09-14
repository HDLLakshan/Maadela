package com.example.maadela;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class OrderList extends ArrayAdapter<OrderClass> {
    private Activity context;
    private List<OrderClass> orderList;

    OrderClass orderClass;

    public OrderList(Activity context, List<OrderClass>orderList){
        super(context, R.layout.orderlist, orderList);
        this.context = context;
        this.orderList=orderList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate( R.layout.orderlist,null,true );
        orderClass = orderList.get( position );

        TextView type = (TextView) listViewItem.findViewById( R.id.type );
        TextView amount = (TextView) listViewItem.findViewById( R.id.amount );
        TextView date = (TextView) listViewItem.findViewById( R.id.date);



        type.setText( "  Ordered Type : "+orderClass.getType() );
        amount.setText( "  Amount :  "+new Double(orderClass.getAmount()).toString()+"/kg" );
        date.setText("  Requested Date : "+orderClass.getDate() );





        return listViewItem;
    }
}