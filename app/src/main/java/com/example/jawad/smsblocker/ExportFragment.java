package com.example.jawad.smsblocker;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.jawad.smsblocker.FileBrowserActivity.INTENT_ACTION_SELECT_DIR;


public class ExportFragment extends Fragment {

    AppCompatButton exportBtn;
    private final int REQUEST_CODE_PICK_DIR = 1;
    BlockListModel blockListModel;
    ProgressBar pBar;

    public ExportFragment() {
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
        View v = inflater.inflate(R.layout.fragment_export, container, false);

        blockListModel = new BlockListModel(this.getContext());
        exportBtn = (AppCompatButton) v.findViewById(R.id.export_button);
        final Activity activityForButton = this.getActivity();
        pBar = (ProgressBar) v.findViewById(R.id.progressBar2);


        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileExploreIntent = new Intent(
                        INTENT_ACTION_SELECT_DIR,
                        null,
                        activityForButton,
                        FileBrowserActivity.class
                );
                startActivityForResult(
                        fileExploreIntent,
                        REQUEST_CODE_PICK_DIR
                );
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_CODE_PICK_DIR) {
            if(resultCode == RESULT_OK) {
                String newDir = data.getStringExtra(
                        FileBrowserActivity.returnDirectoryParameter);

                pBar.setVisibility(View.VISIBLE);

                File file = new File(newDir+"/SMSBlocker.csv");
                FileOutputStream fOut = null;
                OutputStreamWriter outWriter = null;

                try{
                    file.createNewFile();
                     fOut = new FileOutputStream(file);
                     outWriter = new OutputStreamWriter(fOut);

                    ArrayList<BlockList> arrayList = blockListModel.getAllNumbers("date_desc");

                    for(int i=0;i<arrayList.size();i++){
                        BlockList bl = arrayList.get(i);

                           outWriter.append(bl.get_title_number());

                        outWriter.append("\n");

                    }


                    outWriter.flush();


                    pBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(
                            this.getContext(),
                            "List Exported: "+newDir+"/SMSBlocker.csv",
                            Toast.LENGTH_LONG).show();


                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally{
                    try{
                        fOut.close();
                        outWriter.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
