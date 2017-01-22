package ru.ifmo.droid2016.smstotelegram.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ru.ifmo.droid2016.smstotelegram.R;
import ru.ifmo.droid2016.smstotelegram.Variables;
import ru.ifmo.droid2016.smstotelegram.bot.SMSToTelegramBot;
import ru.ifmo.droid2016.smstotelegram.service.SMSReceiver;
import ru.ifmo.droid2016.smstotelegram.service.SMSReceiverService;

public class MainActivity extends AppCompatActivity {

    Button start, stop;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Variables.bot = new SMSToTelegramBot();
        setContentView(R.layout.activity_main);
        Log.e("Meow", String.valueOf(Variables.activeStart));
        start = (Button)findViewById(R.id.startButton);
        stop = (Button) findViewById(R.id.stopButton);
        intent = new Intent(this, SMSReceiverService.class);
        redrawButtons();
    }

    public void onStartClick(View view) {
        Intent intent1 = new Intent(MainActivity.this, ListOfUsersActivity.class);
        startActivity(intent1);
    }


    public void onStopClick(View view) {

        stopService(intent);
        redrawButtons();
        Log.e("Meow", "stopService");
        /*if (Variables.chat == null) {
            Variables.activeStart = true;
            Log.e("Meow", String.valueOf(Variables.activeStart));
            redrawButtons();
        }*/
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    protected void onResume() {
        startService(intent);
        redrawButtons();
        /*if (Variables.chat != null) {
            Variables.activeStart = false;
            Log.e("Meow", "onResume:" + String.valueOf(Variables.activeStart));
            redrawButtons();
        }*/
        super.onResume();
    }

    private void redrawButtons() {
        if (!isMyServiceRunning(SMSReceiverService.class)) {
            start.setEnabled(true);
            stop.setEnabled(false);
        } else {
            start.setEnabled(false);
            stop.setEnabled(true);
        }
    }
}
