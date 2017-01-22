package ru.ifmo.droid2016.smstotelegram.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ru.ifmo.droid2016.smstotelegram.service.BackService;
import ru.ifmo.droid2016.smstotelegram.R;

import static ru.ifmo.droid2016.smstotelegram.SMSToTelegramApp.intent;

public class MainActivity extends AppCompatActivity {
    private Button startBtn = null;
    private Button stopBtn = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "onCreate");

        setContentView(R.layout.activity_main);
        intent = new Intent(this, BackService.class);
        startBtn = (Button) findViewById(R.id.startButton);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Meow", "Lala");
                startActivity(new Intent(getApplicationContext(),SelectionActivity.class));
                redrawButtons();
            }
        });
        stopBtn = (Button) findViewById(R.id.stopButton);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
                redrawButtons();
            }
        });
        redrawButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Meow", "Start");
        Log.i("MainActivity", "onResume");
        redrawButtons();
    }

    private void redrawButtons() {
        if (!isMyServiceRunning(BackService.class)) {
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
        } else {
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning: ", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning: ", false + "");
        return false;
    }
}