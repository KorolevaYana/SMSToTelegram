package ru.ifmo.droid2016.smstotelegram.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.ifmo.droid2016.smstotelegram.Variables;

/**
 * Created by fox on 21.01.2017.
 */

public class SMSReceiverService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Variables.chat != null) {
            Variables.receiver = new SMSReceiver();
            registerReceiver(Variables.receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
            Log.e("Meow", "Receiver registered");
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Meow", "onDestroy: " + ((Variables.receiver == null) ? "receiver null" : "receiver not null"));
        if (Variables.receiver != null) {
            unregisterReceiver(Variables.receiver);
            Log.e("Meow", "Receiver unregistered.");
        }
        Variables.chat = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
