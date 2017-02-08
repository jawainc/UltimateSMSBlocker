package com.example.jawad.smsblocker;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.Toast;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;



import java.util.ArrayList;


public class AddNumberFragment extends Fragment implements
        View.OnClickListener{


    public static ArrayList<Integer> checkedItems = new ArrayList<Integer>();
    public static ArrayList<Integer> checkedItemsMessage = new ArrayList<Integer>();

    private BottomBar bt_bar;
    private AppCompatEditText addSingleNumberEditor;
    private AppCompatEditText addrangeNumberFromEditor;
    private AppCompatEditText addrangeNumberToEditor;
    private ListViewCompat listView;
    private ListViewCompat listMessages;

    BlockListModel blockListModel;
    BlockMessageModel blockMessageModel;


    ArrayList<ContactList> arrayOfContacts = new ArrayList<ContactList>();
    ContactAdapter adapter;

    ArrayList<PhoneMessageList> arrayOfPhoneMessages = new ArrayList<PhoneMessageList>();
    PhoneMessageAdapter adapter_messages;



    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID




    private OnFragmentInteractionListener mListener;

    public AddNumberFragment() {
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
        final View v =  inflater.inflate(R.layout.fragment_add_number, container, false);

        addSingleNumberEditor = (AppCompatEditText) v.findViewById(R.id.add_single_number_editor);
        addrangeNumberFromEditor = (AppCompatEditText) v.findViewById(R.id.add_range_number_from_editor);
        addrangeNumberToEditor = (AppCompatEditText) v.findViewById(R.id.add_range_number_to_editor);

        adapter = new ContactAdapter(this.getContext(), arrayOfContacts);
        adapter_messages = new PhoneMessageAdapter(this.getContext(), arrayOfPhoneMessages);

        listView = (ListViewCompat) v.findViewById(R.id.contact_list_view);
        listView.setAdapter(adapter);

        listMessages = (ListViewCompat) v.findViewById(R.id.phone_inbox_messages_list_view);
        listMessages.setAdapter(adapter_messages);


        AppCompatButton bt = (AppCompatButton) v.findViewById(R.id.add_single_number_button);
        AppCompatButton rbt = (AppCompatButton) v.findViewById(R.id.add_range_number_button);
        AppCompatButton addNumberFromContactButton = (AppCompatButton) v.findViewById(R.id.addSelectedNumbers);
        AppCompatButton addFromMessages = (AppCompatButton) v.findViewById(R.id.addSelectedMessages);




        bt.setOnClickListener(this);
        rbt.setOnClickListener(this);
        addNumberFromContactButton.setOnClickListener(this);
        addFromMessages.setOnClickListener(this);

        blockListModel = new BlockListModel(this.getContext());
        blockMessageModel = new BlockMessageModel(this.getContext());


        final View singleNumberView = v.findViewById(R.id.add_number_form);
        final View rangeNumberView = v.findViewById(R.id.add_range_number_form);
        final View contactNumberView = v.findViewById(R.id.contact_list_container);
        final View phoneMessagesView = v.findViewById(R.id.phone_messages_container);



        bt_bar = BottomBar.attach(v, savedInstanceState);
        bt_bar.setItemsFromMenu(R.menu.bottom_menu_add_number, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if(menuItemId == R.id.add_single_number){
                    rangeNumberView.setVisibility(View.INVISIBLE);
                    contactNumberView.setVisibility(View.INVISIBLE);
                    phoneMessagesView.setVisibility(View.INVISIBLE);
                    addSingleNumberEditor.setText("");
                    singleNumberView.setVisibility(View.VISIBLE);
                }
                if(menuItemId == R.id.add_range_number){
                    singleNumberView.setVisibility(View.INVISIBLE);
                    contactNumberView.setVisibility(View.INVISIBLE);
                    phoneMessagesView.setVisibility(View.INVISIBLE);
                    addrangeNumberFromEditor.setText("");
                    addrangeNumberToEditor.setText("");
                    rangeNumberView.setVisibility(View.VISIBLE);
                }
                if(menuItemId == R.id.add_contacts){
                    singleNumberView.setVisibility(View.INVISIBLE);
                    rangeNumberView.setVisibility(View.INVISIBLE);
                    phoneMessagesView.setVisibility(View.INVISIBLE);
                    adapter.clear();

                    retrieveContactNumber();

                    contactNumberView.setVisibility(View.VISIBLE);

                }

                if(menuItemId == R.id.add_messages){
                    singleNumberView.setVisibility(View.INVISIBLE);
                    rangeNumberView.setVisibility(View.INVISIBLE);
                    contactNumberView.setVisibility(View.INVISIBLE);

                    adapter_messages.clear();

                    retrievePhoneMessages();

                    phoneMessagesView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {


            }
        });



        return bt_bar;

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


    @Override
    public void onClick(View v){
        if(v.getId() == R.id.add_single_number_button){
            addSingleNumber(v);
        }
        if(v.getId() == R.id.add_range_number_button){
            addRangeNumber(v);
        }
        if(v.getId() == R.id.addSelectedNumbers){
            addSelectedNumbers(v);
        }
        if(v.getId() == R.id.addSelectedMessages){
            addSelectedMessages(v);
        }
    }



    private void addSingleNumber(View v){

        String num = addSingleNumberEditor.getText().toString();

        if(!Helper.isBlank(num)){

            BlockList blockList = new BlockList(num,"",0);

            if(blockListModel.insertNumber(blockList,true) > 0) {
                addSingleNumberEditor.setText("");
                Toast.makeText(getContext(), "Number added to block list", Toast.LENGTH_LONG).show();
            }

        }
        else{
            Toast.makeText(getContext(), "Please enter number", Toast.LENGTH_SHORT).show();
        }



    }

    private void addRangeNumber(View v){
        String num1 = addrangeNumberFromEditor.getText().toString();
        String num2 = addrangeNumberToEditor.getText().toString();

        boolean add_flag = false;

        if(!Helper.isBlank(num1) && !Helper.isBlank(num2)){

            int number_from = Integer.parseInt(num1);
            int number_to = Integer.parseInt(num2);

            if(number_from < number_to) {
                for(int i = number_from;i <= number_to;i++){
                    BlockList blockList = new BlockList(i+"","",0);

                    if(blockListModel.insertNumber(blockList,true) > 0) {
                        add_flag = true;
                    }
                    else{
                        add_flag = false;
                    }
                }
                if(add_flag){
                    addrangeNumberFromEditor.setText("");
                    addrangeNumberToEditor.setText("");
                    Toast.makeText(getContext(), "Numbers added to block list", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getContext(), "Please enter numbers in correct format", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(getContext(), "Please enter both number", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSelectedNumbers(View v){

        for (int pos:checkedItems) {
            ContactList ct = adapter.getItem(pos);


            if(!Helper.isBlank(ct.number)){

                BlockList blockList = new BlockList(ct.number,"",0);

                if(blockListModel.insertNumber(blockList,false) > 0) {
                    Toast.makeText(getContext(), "Number(s) added to block list", Toast.LENGTH_LONG).show();
                }

            }



        }




    }

    private void addSelectedMessages(View v){

        long insert_id;
        boolean msg_flag = false;
        for (int pos:checkedItemsMessage) {
            PhoneMessageList ct = adapter_messages.getItem(pos);


            if(!Helper.isBlank(ct.getAddress())){

                BlockList blockList = new BlockList(ct.getAddress(),"",0);

                insert_id = blockListModel.insertNumber(blockList,false);
                BlockMessage bm = new BlockMessage((int)insert_id,ct.getAddress(),ct.getMsg());
                blockMessageModel.insertMessage(bm,false);

                this.getContext().getContentResolver().delete(
                        Uri.parse("content://sms/" + ct.getId()), null, null);

                msg_flag = true;
            }



        }

        if(msg_flag){
            Toast.makeText(getContext(), "Message(s) added to block list", Toast.LENGTH_LONG).show();
        }




    }

    private void retrievePhoneMessages(){


        Uri message = Uri.parse("content://sms/");


        Cursor c = this.getContext().getContentResolver().query(message, null, null, null, null);



        if (c.moveToFirst()) {

           do  {

                PhoneMessageList phoneMessageList = new PhoneMessageList();
                phoneMessageList.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                phoneMessageList.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                phoneMessageList.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                phoneMessageList.setReadState(c.getString(c.getColumnIndex("read")));
                phoneMessageList.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    phoneMessageList.setFolderName("inbox");
                } else {
                    phoneMessageList.setFolderName("sent");
                }

                adapter_messages.add(phoneMessageList);

            }while(c.moveToNext());
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        try {
            if (!c.isClosed()) {
                c.close();
            }
            c = null;
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
    private void retrieveContactNumber() {



        String contactNumber = null;
        String contactName = null;


        // querying contact data store
        Cursor cursor = this.getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()){
            if("1".equals(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))){
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                // Using the contact ID now we will get contact phone number
                Cursor cursorPhone = this.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                                ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                        new String[]{contactID},
                        null);

                if (cursorPhone.moveToFirst()) {
                    contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }

                try {
                    if (!cursorPhone.isClosed()) {
                        cursorPhone.close();
                    }
                    cursorPhone = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ContactList cl = new ContactList(contactName,contactNumber,Integer.parseInt(contactID));
                adapter.add(cl);

            }


        }

        try {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            cursor = null;
        } catch (Exception e) {
            e.printStackTrace();
        }







    }




}
