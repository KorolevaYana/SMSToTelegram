package ru.ifmo.droid2016.smstotelegram.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.IOException;

import static ru.ifmo.droid2016.smstotelegram.SMSToTelegramApp.bot;

/**
 * Created by Koroleva Yana.
 */

public class SMSService extends Service {

    public static String getContactName(Context context, String phoneNumber) {
        Log.e("Meow", "Finding name");
        ContentResolver cr = context.getContentResolver();
        Log.e("Meow", "kek");
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Log.e("Meow", "lol");
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            Log.e("Meow", "Null cursor");
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        Log.e("Meow", contactName == null ? "not found contact" : contactName);

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String message = "";
        String contactName = getContactName(this, intent.getStringExtra("phone"));
        message += (contactName == null) ? intent.getStringExtra("phone") : contactName;
        message += ":\n";
        message += intent.getStringExtra("message");

        SendMessage request = new SendMessage(intent.getLongExtra("chatId", -1), message);
        Log.e("Meow", "Request");

        bot.execute(request, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {
                Log.e("Meow", "Sending message");
                boolean ok = response.isOk();
                Log.e("Meow", String.valueOf(ok) + ": " + response.toString());
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {
                Log.e("Meow", "Failed");
            }
        });
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
