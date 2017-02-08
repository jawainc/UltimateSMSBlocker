package com.example.jawad.smsblocker;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class ImportFragment extends Fragment {


    Button importBtn;
    private OnFragmentInteractionListener mListener;
    private final int REQUEST_CODE_PICK_FILE = 2;
    BlockListModel blockListModel;
    ProgressBar pBar;

    public ImportFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_import, container, false);

        blockListModel = new BlockListModel(this.getContext());

        final Activity activityForButton = this.getActivity();
        importBtn = (Button) v.findViewById(R.id.import_list_button);
        pBar = (ProgressBar) v.findViewById(R.id.progressBar);

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileExploreIntent = new Intent(
                       FileBrowserActivity.INTENT_ACTION_SELECT_FILE,
                        null,
                        activityForButton,
                        FileBrowserActivity.class
                );
                startActivityForResult(
                        fileExploreIntent,
                        REQUEST_CODE_PICK_FILE
                );
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_CODE_PICK_FILE) {
            if(resultCode == RESULT_OK) {
                String newFile = data.getStringExtra(
                        FileBrowserActivity.returnFileParameter);

                BufferedReader bufferedReader = null;
                pBar.setVisibility(View.VISIBLE);
                /**
                 * reading file
                 */
                try{
                    bufferedReader = new BufferedReader(new FileReader(new File(newFile)));

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] RowData = line.split(",");
                        if(RowData.length > 1){

                            if(!Helper.isBlank(RowData[0]) && !Helper.isBlank(RowData[1])){

                                int number_from = Integer.parseInt(RowData[0]);
                                int number_to = Integer.parseInt(RowData[1]);

                                if(number_from < number_to) {

                                    for(int i = number_from;i <= number_to;i++){
                                        BlockList blockList = new BlockList(i+"","",0);

                                        blockListModel.insertNumber(blockList,true);

                                    }

                                }

                            }

                        }
                        else{
                            if(!Helper.isBlank(RowData[0])){

                                BlockList blockList = new BlockList(RowData[0],"",0);
                                blockListModel.insertNumber(blockList,false);

                            }

                        }

                    }
                    pBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(
                            this.getContext(),
                            "Numbers imported successfully",
                            Toast.LENGTH_LONG).show();

                }
                catch (Exception e){
                    Toast.makeText(
                            this.getContext(),
                            "Unable to load file!",
                            Toast.LENGTH_LONG).show();

                }
                finally{


                    try{
                        bufferedReader.close();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                }




            }
        }



        super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
