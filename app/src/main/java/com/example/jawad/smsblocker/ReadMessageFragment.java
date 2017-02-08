package com.example.jawad.smsblocker;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ReadMessageFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    int msg_id;
    String number;
    BlockMessageModel bmm;
    ImageButton delete;


    public ReadMessageFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_read_message, container, false);

        Bundle bd = this.getArguments();
        msg_id = bd.getInt("blockListId");
        number = bd.getString("number");
        ArrayList<BlockMessage> arrayOfMessages;

        bmm = new BlockMessageModel(this.getContext());
        if (msg_id == 0)
            arrayOfMessages = bmm.getReadMessagesForNumber(number);
        else
            arrayOfMessages = bmm.getReadMessages(Integer.toString(msg_id));

        ReadMessageAdapter adapter = new ReadMessageAdapter(this.getContext(),arrayOfMessages);
        ListView listView = (ListView) v.findViewById(R.id.readMessagesListView);

        listView.setAdapter(adapter);


        delete = (ImageButton) v.findViewById(R.id.delete_message);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle("Are You Sure?");
                builder.setMessage("Message will be deleted");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if(msg_id == 0){
                            if(bmm.removeMessagesForNumber(number)){
                                Toast.makeText(getContext(), "Message Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            if(bmm.removeMessages(msg_id)){
                                Toast.makeText(getContext(), "Message Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }

                        dialog.dismiss();
                        getFragmentManager().popBackStack();
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


        mListener.onFragmentInteraction(Uri.parse("ReadeMessage?fragment=ReadMessage&number="+bd.getString("number")));

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
