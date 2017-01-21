package ru.ifmo.droid2016.smstotelegram.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ru.ifmo.droid2016.smstotelegram.R;
import ru.ifmo.droid2016.smstotelegram.Variables;
import ru.ifmo.droid2016.smstotelegram.model.User;

/**
 * Created by fox on 21.01.2017.
 */

public class ListOfUsersActivity extends ListActivity {
    class Closure {
        boolean getFailure = false;
        List<Update> updates = new ArrayList<>();
        ListActivity activity;
        private Closure(){}
    }
    final Closure closure = new Closure();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list_of_users);

        closure.activity = this;

        final Handler handler = new Handler() {
            public void handleMessage(android.os.Message message) {
                List<User> users = new ArrayList<>();
                List<String> namesContacts = new ArrayList<>();
                HashSet<Long> ids = new HashSet<>();

                for (Update update : closure.updates) {
                    Log.e("Meow", update.message().chat() == null ? "fyr" : "kek");
                    if (!ids.contains(update.message().chat().id())) {
                    /*contacts.add(new Contact(update.message().chat().firstName(),
                            update.message().chat().lastName(),
                            update.message().chat().username()));*/
                        long id = update.message().chat().id();
                        ids.add(id);
                        if (update.message().chat().title() != null) {
                            users.add(new User(update.message().chat().title(), "", "", id));
                        } else {
                            users.add(new User(update.message().chat().firstName(),
                                    update.message().chat().lastName(),
                                    "@" + update.message().chat().username(), id));
                        }
                    }
                }
                Log.e("Meow", String.valueOf(users.size()));

                if (closure.activity == null) {
                    Log.e("Meow", "Null activity");
                }
                setListAdapter(new ListOfUsersAdapter(closure.activity, users));
            }
        };

        GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);

        if (Variables.bot != null) {
            Variables.bot.bot.execute(getUpdates, new Callback<GetUpdates, GetUpdatesResponse>() {
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
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Variables.chat = closure.updates.get(position).message().chat();
        finish();
    }
}
