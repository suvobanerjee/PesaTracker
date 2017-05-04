package com.excogit.suvo.pesatracker.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.constants.StringConstants;
import com.excogit.suvo.pesatracker.lsiteners.SMSListener;
import com.excogit.suvo.pesatracker.services.SMSSaveService;

/**
 * Created by suvo on 3/18/2017.
 */

public class SMSReceiver extends BroadcastReceiver {

    private static SMSListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle data = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for (int i =0; i<pdus.length;i++)
        {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            String message = smsMessage.getMessageBody();

            Log.e("Receiver*sender",sender);

            if(sender.equals("+61419410954"))
            {
                Log.e("Receiver sender",sender);
                mListener.SMSReceived(message);
            }
            else if(sender.equals(StringConstants.SENDER_AIRTEL) || sender.equals(StringConstants.SENDER_MTN)
             || sender.equals(StringConstants.SENDER_AFRICEL) || sender.equals(StringConstants.SENDER_MPESA))
            //else if(sender.equals("9004049239"))
            {
                //start the service here
                //and pass the message body to the service
                long timestamp = smsMessage.getTimestampMillis();

                serviceStarter(sender,message,context,timestamp);
            }
        }
    }

    public static void bindListner(SMSListener listener)
    {
        mListener = listener;
    }

    private void serviceStarter(String sender, String msg,Context context,long timestamp)
    {
        Intent smsServiceIntent = new Intent(context, SMSSaveService.class);
        smsServiceIntent.putExtra(StringConstants.SENDER,sender);
        smsServiceIntent.putExtra(StringConstants.MSG_BODY,msg);
        smsServiceIntent.putExtra(StringConstants.TIMESTAMP,timestamp);

        context.startService(smsServiceIntent);
    }
}
