package ru.ifmo.droid2016.smstotelegram.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import ru.ifmo.droid2016.smstotelegram.R;
import ru.ifmo.droid2016.smstotelegram.SMSToTelegramApp;
import ru.ifmo.droid2016.smstotelegram.model.User;

import static ru.ifmo.droid2016.smstotelegram.SMSToTelegramApp.bot;
import static ru.ifmo.droid2016.smstotelegram.SMSToTelegramApp.chat;
import static ru.ifmo.droid2016.smstotelegram.SMSToTelegramApp.intent;

/**
 * Created by fox on 22.01.2017.
 */

public class SelectionActivity extends Activity {
    class Closure {
        boolean getFailure = false;
        String key = null;
        List<Update> updates = new ArrayList<>();

        private Closure() {
        }
    }

    HashSet<User> users = new HashSet<>();
    List<String> usernames = new ArrayList<>();
    List<Chat> chats = new ArrayList<>();
    final Closure closure = new Closure();

    EditText editText;
    Button okBtn;

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();

    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        editText = (EditText) findViewById(R.id.editText);
        okBtn = (Button) findViewById(R.id.okButton);

        Log.e("Meow", "Selection OnCreate");
        String appName = "org.telegram.messenger";
        boolean isInstalled = isAppAvailable(getApplicationContext(), appName);

        if (isInstalled) {
            Log.e("Meow", "isInstalled");
            Intent tmpIntent = new Intent(Intent.ACTION_VIEW);

            closure.key = randomString(22);
            Log.e("meow", "key: " + closure.key);

            tmpIntent.setData(Uri.parse("https://telegram.me/sms_to_telegram_bot?start=" + closure.key));
            startActivity(tmpIntent);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Telegram isn't installed.", Toast.LENGTH_LONG);
            toast.show();
        }


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findUsers();

            }
        });
    }

    void findUsers() {
        final Handler handler = new Handler() {
            public void handleMessage(android.os.Message message) {
                String defaultUsername = null;
                if (closure.updates != null) {
                    for (Update update : closure.updates) {
                        Log.e("meow", "message");
                        Log.e("meow", "  username: " + update.message().chat().username());
                        Log.e("meow", "  text: " + update.message().text());
                        Log.e("Meow", update.message().chat() == null ? "fyr" : "kek");
                        if (update.message().chat().username() == null)
                            continue;
                        User user = new User(update.message().chat().firstName(),
                                update.message().chat().lastName(),
                                "@" + update.message().chat().username(),
                                update.message().chat().id());
                        if (!users.contains(user)) {
                            users.add(user);
                            usernames.add(user.username);
                            chats.add(update.message().chat());
                        }
                        if (!update.message().text().startsWith("/"))
                            continue;
                        String[] command = update.message().text().split(" ");
                        Log.e("meow", "command: " + command[0]);
                        Log.e("meow", "command: " + command[1]);
                        if (command.length >= 2 && command[0].equals("/start") && command[1].equals(closure.key)) {
                            defaultUsername = "@" + update.message().chat().username();
                        }
                    }
                } else {
                    Log.e("Meow", "Problems with connecting to Telegram. Probably old app version.");
                }

                Log.e("meow", "default username:" + defaultUsername);
                if (defaultUsername != null && usernames.contains(defaultUsername)) {
                    int index = usernames.indexOf(defaultUsername);
                    chat = chats.get(index);
                    startService(intent);

                    finish();
                    Toast toast = Toast.makeText(getApplicationContext(), "connected to: " + defaultUsername, Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                String username = editText.getText().toString();
                if (usernames.contains(username)) {
                    int index = usernames.indexOf(username);
                    chat = chats.get(index);
                    startService(intent);

                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wrong username or bot has no access.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        };

        GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);

        if (bot != null) {
            bot.execute(getUpdates, new Callback<GetUpdates, GetUpdatesResponse>() {
                @Override
                public void onResponse(GetUpdates request, GetUpdatesResponse response) {
                    Log.e("Meow", "onResponse, is ok: " + (response.isOk() ? "true" : "false"));
                    Log.e("Meow", "response description: " + response.description());
                    closure.updates = response.updates();

                    Log.e("Meow", "lala" + ((closure.updates == null) ? "1" : "2"));
                    Log.e("Meow", "mur");
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onFailure(GetUpdates request, IOException e) {
                    closure.getFailure = true;
                }
            });

        } else {
            Toast toast = Toast.makeText(this, "Problems with bot initialization.", Toast.LENGTH_LONG);
            toast.show();
        }

        if (closure.getFailure) {
            Toast toast = Toast.makeText(this, "Getting updates failure.", Toast.LENGTH_LONG);
            toast.show();
            closure.getFailure = false;
            finish();
        }

    }

    private boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }
}
