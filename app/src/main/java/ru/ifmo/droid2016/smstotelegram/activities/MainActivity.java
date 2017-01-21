package ru.ifmo.droid2016.smstotelegram.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.ifmo.droid2016.smstotelegram.R;
import ru.ifmo.droid2016.smstotelegram.Variables;
import ru.ifmo.droid2016.smstotelegram.bot.SMSToTelegramBot;
import ru.ifmo.droid2016.smstotelegram.service.SMSReciever;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Variables.bot = new SMSToTelegramBot();
        setContentView(R.layout.activity_main);
    }

    public void onStartClick(View view) {
        Intent intent = new Intent(MainActivity.this, ListOfUsersActivity.class);
        startActivity(intent);
    }


    public void onStopClick(View view) {
        if (Variables.receiver != null && Variables.receiverRegistered) {
            unregisterReceiver(Variables.receiver);
            Variables.receiverRegistered = false;
            Log.e("Meow", "Receiver unregistered.");
        }
    }

    @Override
    protected void onDestroy() {
        if (Variables.receiver != null && Variables.receiverRegistered) {
            unregisterReceiver(Variables.receiver);
            Variables.receiverRegistered = false;
            Log.e("Meow", "onDestroy: Receiver unregistered.");
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (Variables.chat != null && !Variables.receiverRegistered) {
            Variables.receiver = new SMSReciever();
            registerReceiver(Variables.receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
            Variables.receiverRegistered = true;
            Log.e("Meow", "Receiver registered");
        }
        super.onResume();
    }
}
