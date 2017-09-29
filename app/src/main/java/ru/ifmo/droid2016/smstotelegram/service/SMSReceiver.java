package ru.ifmo.droid2016.smstotelegram.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import static ru.ifmo.droid2016.smstotelegram.SMSToTelegramApp.chat;

/**
 * Created by Koroleva Yana.
 */

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("meow", "SMSReceiver::onReceive ()");
        SmsMessage[] smsMessages;
        if (Build.VERSION.SDK_INT >= 19) {
            smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        } else {
            smsMessages = getMessagesFromIntent(intent);
        }

        if (smsMessages != null) {
            String s = "";
            for (int i = 0; i < smsMessages.length; i++) {
                s += smsMessages[i].getMessageBody();
            }

            Intent newIntent = new Intent(context, SMSService.class);
            newIntent.putExtra("phone", smsMessages[0].getOriginatingAddress());
            newIntent.putExtra("message", s);
            newIntent.putExtra("chatId", chat.id());
            context.startService(newIntent);
        }
    }

    public static SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }


}
