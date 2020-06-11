package in.sanitization.sanitization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import in.sanitization.sanitization.util.SmsListener;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 12,December,2019
 */
public class SmsReceiver extends BroadcastReceiver {
    private static SmsListener mListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        //interface

        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
//            //Check the sender to filter messages which we require to read
//            String messageBody = smsMessage.getMessageBody();
//
//                //Pass the message text to interface
//                mListener.messageReceived(messageBody);
//
//
            if (sender.equals("VM-BNPLUS"))
            {

                String messageBody = smsMessage.getMessageBody();

                //Pass the message text to interface
                mListener.messageReceived(messageBody);

            }
//            else
//            {
//                String messageBody ="nothing";
//
//                //Pass the message text to interface
//                mListener.messageReceived(messageBody);
//            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
