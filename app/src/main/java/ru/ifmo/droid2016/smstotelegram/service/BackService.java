package ru.ifmo.droid2016.smstotelegram.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import static ru.ifmo.droid2016.smstotelegram.SMSToTelegramApp.receiver;

/**
 * Created by fox on 22.01.2017.
 */

public class BackService extends Service {
    public BackService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("BackService", "onStartCommand");
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("BackService", "onCreate");
        receiver = new SMSReceiver();
        if (Build.VERSION.SDK_INT >= 19) {
            registerReceiver(receiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION), null, new Handler());
        } else {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(receiver, new IntentFilter(), null, new Handler());
        }
        Log.e("BackService", "Receiver registered");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("BackService", "onDestroy");
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
