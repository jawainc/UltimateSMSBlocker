package com.example.jawad.smsblocker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jawad on 6/17/2016.
 */
public class ReadMessageAdapter extends ArrayAdapter<BlockMessage> {

    public ReadMessageAdapter(Context ct, ArrayList<BlockMessage> bl){
        super(ct,0,bl);
    }

    private int item;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BlockMessage list = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.read_message_item, parent, false);
        }
        // Lookup view for data population

        TextView tvDate = (TextView) convertView.findViewById(R.id.read_message_date);
        TextView tvMessage = (TextView) convertView.findViewById(R.id.reade_message_message);

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");




        tvDate.setText(df.format(list.get_created_at()));

        if(list.get_message().length() > 135)
            tvMessage.setText(list.get_message().substring(0,135) + "...");
        else
            tvMessage.setText(list.get_message());





        // Return the completed view to render on screen
        return convertView;
    }


}
