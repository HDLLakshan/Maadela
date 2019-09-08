package com.example.maadela;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FishList extends ArrayAdapter<DailySelling> {
    private Activity context;
    private List<DailySelling> fishList;
    String timeopend;
    String timenow ;
    Date d1=null;
    Date d2=null;
    DailySelling dailySelling;

    public FishList(Activity context, List<DailySelling>fishList){
        super(context, R.layout.fist_list_layout, fishList);
        this.context = context;
        this.fishList=fishList;
        System.out.println( "Kalalalalalalal" );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate( R.layout.fist_list_layout,null,true );
         dailySelling = fishList.get( position );

        TextView name = (TextView) listViewItem.findViewById( R.id.fname );
        TextView rate = (TextView) listViewItem.findViewById( R.id.rate );
        TextView time = (TextView) listViewItem.findViewById( R.id.time );


        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        timenow = new SimpleDateFormat("HH:mm").format(new Date());
        timeopend = dailySelling.getTime();

        try {
            d1 = format.parse( timeopend );
            d2 = format.parse(timenow);
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e){

        }


        long diff = d2.getTime() - d1.getTime();


        long diffHours = diff / (60 * 60 * 1000);
        long diffMinutes = (diff / (60 * 1000))-(diffHours*60);

        System.out.println("Time in minutes: " + diffMinutes + " minutes.");
        System.out.println("Time in hours: " + diffHours + " hours.");
        name.setText( dailySelling.getFishname() );
        rate.setText( "Rs "+new Double(dailySelling.getRate()).toString()+"/kg" );
        time.setText( (int) diffHours +":Hrs "+ diffMinutes + ":Min   Ago" );
       // name.setText( "laka" );
;




        return listViewItem;
    }
}
