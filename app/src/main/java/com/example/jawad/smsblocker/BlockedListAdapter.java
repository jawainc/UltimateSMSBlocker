package com.example.jawad.smsblocker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jawad on 6/17/2016.
 */
public class BlockedListAdapter extends ArrayAdapter<BlockList> {

    public BlockedListAdapter(Context ct, ArrayList<BlockList> bl){
        super(ct,0,bl);
    }

    private int item;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BlockList list = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_block_list, parent, false);
        }
        // Lookup view for data population
        TextView tvNumber = (TextView) convertView.findViewById(R.id.block_list_number_1);

        tvNumber.setTag(list.get_id());

        if(list.is_ranged_number()){
            tvNumber.setText(list.get_title_number()+"  ....  "+list.get_title_range_number());
        }
        else{
            tvNumber.setText(list.get_title_number());
        }

        // Return the completed view to render on screen
        return convertView;
    }


}
