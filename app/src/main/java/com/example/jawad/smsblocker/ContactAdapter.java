package com.example.jawad.smsblocker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by jawad on 6/15/2016.
 */
public class ContactAdapter extends ArrayAdapter<ContactList> {

    public ContactAdapter(Context ct, ArrayList<ContactList> al){
        super(ct,0,al);
    }
    private int item;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ContactList contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact_list, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.listName);
        TextView tvNumber = (TextView) convertView.findViewById(R.id.listNumber);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.listId);
        // Populate the data into the template view using the data object
        tvName.setText(contact.name);
        tvNumber.setText(contact.number);
        cb.setTag(Integer.valueOf(position));

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   item = (int) buttonView.getTag();
                    AddNumberFragment.checkedItems.add(item);
                }
                else if(!isChecked){
                    item = (int) buttonView.getTag();
                    AddNumberFragment.checkedItems.remove(Integer.valueOf(item));
                }
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

}
