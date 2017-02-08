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
 * Created by jawad on 6/15/2016.
 */
public class PhoneMessageAdapter extends ArrayAdapter<PhoneMessageList> {

    public PhoneMessageAdapter(Context ct, ArrayList<PhoneMessageList> pl){
        super(ct,0,pl);
    }
    private int item;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PhoneMessageList message = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_phone_message_list, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.listMessage);
        TextView tvNumber = (TextView) convertView.findViewById(R.id.listMessageNumber);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.listMessageId);
        // Populate the data into the template view using the data object
        tvName.setText(message.getMsg());
        tvNumber.setText(message.getAddress());
        cb.setTag(Integer.valueOf(position));

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   item = (int) buttonView.getTag();
                    AddNumberFragment.checkedItemsMessage.add(item);
                }
                else if(!isChecked){
                    item = (int) buttonView.getTag();
                    AddNumberFragment.checkedItemsMessage.remove(Integer.valueOf(item));
                }
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

}
