package ru.ifmo.droid2016.smstotelegram.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import static ru.ifmo.droid2016.smstotelegram.SMSToTelegramApp.chat;

/**
 * Created by Koroleva Yana.
 */

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        SmsMessage smsMessage = smsMessages[0];
        Intent newIntent = new Intent(context, SMSService.class);
        newIntent.putExtra("phone", smsMessage.getOriginatingAddress());
        newIntent.putExtra("message", smsMessage.getMessageBody());
        newIntent.putExtra("chatId", chat.id());
        context.startService(newIntent);
    }
}
