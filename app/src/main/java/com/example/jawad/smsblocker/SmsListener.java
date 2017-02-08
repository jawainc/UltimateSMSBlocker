package com.example.jawad.smsblocker;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;


/**
 * Created by jawad on 6/19/2016.
 */
public class SmsListener extends BroadcastReceiver {

    BlockListModel db;
    SettingModel settingModel;


    @Override
    public void onReceive(Context context, Intent intent) {

        db = new BlockListModel(context);
        settingModel = new SettingModel(context);

        // Get the object of SmsManager
        final SmsManager sms = SmsManager.getDefault();

            Boolean number_blocked = false;
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getOriginatingAddress();




                            int num_id = db.findNumber(phoneNumber);

                            if(num_id > 0){

                                this.blockMessage(currentMessage,context,num_id,phoneNumber);
                                number_blocked = true;
                            }

                       if (!number_blocked){
                            int[] settings = settingModel.getValues();

                            if(settings.length > 0) {
                                if (settings[0] == 1){
                                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
                                    ContentResolver contentResolver = context.getContentResolver();
                                    Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID,
                                            ContactsContract.PhoneLookup.NUMBER }, null, null, null);

                                    try{
                                        if(contactLookup == null || contactLookup.getCount() == 0){
                                            this.blockMessage(currentMessage,context,0,phoneNumber);
                                            number_blocked = true;
                                        }
                                    }
                                    finally {
                                        contactLookup.close();
                                    }

                                }
                            }
                        }


                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }

    }

    void blockMessage(SmsMessage currentMessage, Context context, int num_id,String phoneNumber){

        String message = currentMessage.getDisplayMessageBody();
        BlockMessage bm = new BlockMessage(num_id,phoneNumber,message);
        BlockMessageModel bmm = new BlockMessageModel(context);
        bmm.insertMessage(bm,false);

        this.abortBroadcast();

    }

}
