package com.example.maadela;


import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RequestList extends ArrayAdapter<Requests> {

    private Activity context;
    private List<Requests> requestsList;

    public RequestList(Activity context, List<Requests> requestsList){
        super(context, R.layout.requestlist_layout, requestsList);
        this.context=context;
        this.requestsList=requestsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listviweItem = inflater.inflate( R.layout.requestlist_layout,null,true );

        TextView cusn = (TextView)listviweItem.findViewById( R.id.cname );
       TextView fname= (TextView)listviweItem.findViewById( R.id.fname );
       TextView amount= (TextView)listviweItem.findViewById( R.id.amount);
       TextView rtimed = (TextView)listviweItem.findViewById( R.id.rtime);
       TextView sts = (TextView)listviweItem.findViewById( R.id.status );
        Requests requests = requestsList.get( position );


        if(requests.getStatus().equals( "Confirmed" ))
            listviweItem.setBackgroundColor( Color.GREEN );
        if(requests.getStatus().equals( "Reject" ))
            listviweItem.setBackgroundColor( Color.RED );
        if(requests.getStatus().equals( "Pending" ))
            listviweItem.setBackgroundColor( Color.BLUE );

        fname.setText( requests.getFishname() );
        amount.setText( requests.getAmount() );
        cusn.setText( requests.getShopname() );
        rtimed.setText( requests.getTime() );
        sts.setText(requests.getStatus());


        return listviweItem;
    }
}
