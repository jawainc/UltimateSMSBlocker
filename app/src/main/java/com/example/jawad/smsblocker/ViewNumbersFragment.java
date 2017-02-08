package com.example.jawad.smsblocker;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.PopupMenu;
import android.view.*;
import android.widget.*;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;


public class ViewNumbersFragment extends Fragment {


    BlockListModel blockListModel;
    BlockMessageModel blockMessageModel;
    ArrayList<BlockList> arrayOfNumbers;
    BlockedListAdapter adapter;
    SwipeMenuListView listView;
    ImageButton filterBtn;

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getContext());
            // set item background
            deleteItem.setBackground(R.color.colorAccent);
            // set item width
            deleteItem.setWidth(210);
            // set a icon
            deleteItem.setIcon(R.drawable.ic_action_trash);


            // create "delete" item
            SwipeMenuItem editItem = new SwipeMenuItem(getContext());
            // set item background
            editItem.setBackground(R.color.colorPrimary);
            // set item width
            editItem.setWidth(210);
            // set a icon
            editItem.setIcon(R.drawable.ic_mode_edit);

            // add to menu
            menu.addMenuItem(deleteItem);
            menu.addMenuItem(editItem);
        }
    };

    private OnFragmentInteractionListener mListener;

    public ViewNumbersFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_view_numbers, container, false);

        blockListModel = new BlockListModel(this.getContext());
        blockMessageModel = new BlockMessageModel(this.getContext());

        filterBtn = (ImageButton) v.findViewById(R.id.filterBtn);



        arrayOfNumbers = blockListModel.getAllNumbers("date_desc");


        try{

            adapter = new BlockedListAdapter(this.getContext(), arrayOfNumbers);

            listView = (SwipeMenuListView) v.findViewById(R.id.listNumberView);
            listView.setMenuCreator(creator);
            listView.setAdapter(adapter);
            listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);


            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            // delete

                            final BlockList item = adapter.getItem(position);
                            if(blockListModel.removeItem(item.get_id())){
                                adapter.remove(item);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Number Un-Blocked", Toast.LENGTH_SHORT).show();

                                blockMessageModel.removeMessages(item.get_id());

                            }

                            break;

                        case 1:

                            // edit

                            final BlockList item_edit = adapter.getItem(position);
                            String number_edit = item_edit.get_title_number();

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                            builder.setTitle("Edit Number");
                            final EditText edittext = new EditText(getContext());
                            edittext.setText(number_edit);
                            builder.setView(edittext);

                            builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    String number_edit = edittext.getText().toString();

                                    if(blockListModel.upDateNumber(""+item_edit.get_id(),number_edit)){
                                        adapter.clear();
                                        arrayOfNumbers = blockListModel.getAllNumbers("date_desc");
                                        adapter.addAll(arrayOfNumbers);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(getContext(), "Number Updated", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getContext(), "Unable to change number", Toast.LENGTH_SHORT).show();
                                    }

                                    dialog.dismiss();
                                }
                            });

                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Do nothing
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();

                            break;

                    }
                    // false : close the menu; true : not close the menu
                    return false;
                }
            });

        }
        catch (Exception e){
            System.out.println(e);
        }


        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.filter_menu, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.sort_asc:
                                adapter.clear();
                                arrayOfNumbers = blockListModel.getAllNumbers("num_asc");
                                adapter.addAll(arrayOfNumbers);
                                adapter.notifyDataSetChanged();
                                return true;
                            case R.id.sort_desc:
                                adapter.clear();
                                    arrayOfNumbers = blockListModel.getAllNumbers("num_desc");
                                adapter.addAll(arrayOfNumbers);
                                    adapter.notifyDataSetChanged();
                                return true;
                            case R.id.sort_date_asc:
                                adapter.clear();
                                    arrayOfNumbers = blockListModel.getAllNumbers("date_desc");
                                adapter.addAll(arrayOfNumbers);
                                    adapter.notifyDataSetChanged();
                                return true;
                            case R.id.sort_date_desc:
                                adapter.clear();
                                arrayOfNumbers = blockListModel.getAllNumbers("date_asc");
                                adapter.addAll(arrayOfNumbers);
                                adapter.notifyDataSetChanged();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




}
