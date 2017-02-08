package com.example.jawad.smsblocker;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InboxFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxFragment extends Fragment {

    BlockMessageModel blockMessageModel;
    ArrayList<BlockMessage> arrayOfMessages;
    BlockedMessageAdapter adapter;
    SwipeMenuListView listView;

    ImageButton delete_all;
    TextView tv;

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
            // add to menu
            menu.addMenuItem(deleteItem);


        }
    };

    private OnFragmentInteractionListener mListener;

    public InboxFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inbox, container, false);

        blockMessageModel = new BlockMessageModel(this.getContext());
        arrayOfMessages = blockMessageModel.getAllMessages();
        tv = (TextView) v.findViewById(R.id.inbox_no_message_text);
        delete_all = (ImageButton) v.findViewById(R.id.delete_all_messages);

        if (arrayOfMessages.size() > 0){

            delete_all.setVisibility(View.VISIBLE);
            delete_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    builder.setTitle("Are You Sure?");
                    builder.setMessage("All messages will be deleted, do you want to continue.");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            removeAllMessages();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });
        }
        else{
            tv.setVisibility(View.VISIBLE);
        }





        try{

            adapter = new BlockedMessageAdapter(this.getContext(), arrayOfMessages);

            listView = (SwipeMenuListView) v.findViewById(R.id.viewMessagesContainer);
            listView.setMenuCreator(creator);
            listView.setAdapter(adapter);
            listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BlockMessage bm = adapter.getItem(position);
                    Bundle bundle = new Bundle();

                    bundle.putInt("messageId", bm.get_id());
                    bundle.putInt("blockListId", bm.get_block_list_id());
                    if(bm.get_is_range() == 1)
                        bundle.putString("number", bm.get_number()+"("+bm.get_title_number()+"..."+bm.get_title_range_number()+")");
                    else{
                        if (bm.get_block_list_id() == 0)
                            bundle.putString("number", bm.get_number());
                        else
                            bundle.putString("number", bm.get_title_number());
                    }


                    ReadMessageFragment fragment = new ReadMessageFragment();
                    fragment.setArguments(bundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                   // fragmentManager.beginTransaction().replace(R.id.inboxFragment, fragment).commit();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.hide(InboxFragment.this);
                    fragmentTransaction.replace(R.id.flContent, fragment);
                    fragmentTransaction.commit();
                }
            });

            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            // delete

                            BlockMessage item = adapter.getItem(position);
                            if(blockMessageModel.removeMessages(item.get_block_list_id())){
                                adapter.remove(item);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Message Deleted", Toast.LENGTH_SHORT).show();
                            }

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


        mListener.onFragmentInteraction(Uri.parse("InboxFragment?fragment=Inbox"));

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

    public void removeAllMessages(){

        if(blockMessageModel.removeAllMessages()){
            Toast.makeText(this.getContext(), "Messages Deleted", Toast.LENGTH_SHORT).show();
            adapter.clear();
            adapter.notifyDataSetChanged();
            tv.setVisibility(View.VISIBLE);
            delete_all.setVisibility(View.INVISIBLE);
        }

    }

}
