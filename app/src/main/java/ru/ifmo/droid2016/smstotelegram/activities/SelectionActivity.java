package ru.ifmo.droid2016.smstotelegram.activities;

import android.app.Activity;
import android.content.Intent;
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

import ru.ifmo.droid2016.smstotelegram.R;
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
        List<Update> updates = new ArrayList<>();

        private Closure(){}
    }
    HashSet<User> users = new HashSet<>();
    List<String> usernames = new ArrayList<>();
    List<Chat> chats = new ArrayList<>();
    final Closure closure = new Closure();

    EditText editText;
    Button okBtn;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        editText =(EditText) findViewById(R.id.editText);
        okBtn = (Button) findViewById(R.id.okButton);

        final Handler handler = new Handler() {
            public void handleMessage(android.os.Message message) {
                for (Update update : closure.updates) {
                    Log.e("Meow", update.message().chat() == null ? "fyr" : "kek");
                    if (update.message().chat().username() != null) {
                        User user = new User(update.message().chat().firstName(),
                                    update.message().chat().lastName(),
                                    "@" + update.message().chat().username(),
                                    update.message().chat().id());
                        if (!users.contains(user)) {
                            users.add(user);
                            usernames.add(user.username);
                            chats.add(update.message().chat());
                        }

                    }
                }
            }
        };

        GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);

        if (bot != null) {
            bot.execute(getUpdates, new Callback<GetUpdates, GetUpdatesResponse>() {
                @Override
                public void onResponse(GetUpdates request, GetUpdatesResponse response) {
                    Log.e("Meow", "onResponse");
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


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editText.getText().toString();
                if (usernames.contains(username)) {
                    int index = usernames.indexOf(username);
                    chat = chats.get(index);
                    startService(intent);

                    SendMessage request = new SendMessage(chat.id(), "Connected.");
                    bot.execute(request, new Callback<SendMessage, SendResponse>() {
                        @Override
                        public void onResponse(SendMessage request, SendResponse response) {
                            Log.e("Meow", "Sending message");
                            boolean ok = response.isOk();
                            Log.e("Meow", String.valueOf(ok) + ": " + response.toString());
                        }

                        @Override
                        public void onFailure(SendMessage request, IOException e) {
                            Log.e("Meow", "Failure");
                            Toast toast = Toast.makeText(getApplicationContext(), "Wrong username or bot has no access.", Toast.LENGTH_LONG);
                            toast.show();
                            closure.getFailure = true;
                        }
                    });
                    if (!closure.getFailure) {
                        finish();
                    } else {
                        closure.getFailure = false;
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wrong username or bot has no access.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
