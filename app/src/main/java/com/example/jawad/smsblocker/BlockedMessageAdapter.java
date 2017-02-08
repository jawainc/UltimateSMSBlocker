package com.example.jawad.smsblocker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jawad on 6/17/2016.
 */
public class BlockedMessageAdapter extends ArrayAdapter<BlockMessage> {

    public BlockedMessageAdapter(Context ct, ArrayList<BlockMessage> bl){
        super(ct,0,bl);
    }

    private int item;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BlockMessage list = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_messages_list, parent, false);
        }
        // Lookup view for data population
        TextView tvNumber = (TextView) convertView.findViewById(R.id.message_list_number);
        TextView tvDate = (TextView) convertView.findViewById(R.id.message_list_date);
        TextView tvMessage = (TextView) convertView.findViewById(R.id.message_list_message);



        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");

       if(list.get_is_range() == 0){
           if(list.get_block_list_id() == 0)
               tvNumber.setText(list.get_number()+" (UNKNOWN)");
           else
               tvNumber.setText(list.get_title_number());
       }
       else if(list.get_is_range() == 1){
           tvNumber.setText(list.get_number()+" ("+list.get_title_number()+"..."+list.get_title_range_number()+")");
       }
        tvDate.setText(df.format(list.get_created_at()));

        if(list.get_message().length() > 135)
            tvMessage.setText(list.get_message().substring(0,135) + "...");
        else
            tvMessage.setText(list.get_message());



        if(list.get_is_new() == 1){
            ImageView ivNewMessage = (ImageView) convertView.findViewById(R.id.new_msg_icon);
            ivNewMessage.setVisibility(View.VISIBLE);
        }



        // Return the completed view to render on screen
        return convertView;
    }


}
