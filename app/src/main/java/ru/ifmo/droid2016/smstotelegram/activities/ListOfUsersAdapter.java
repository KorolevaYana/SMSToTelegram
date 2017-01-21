package ru.ifmo.droid2016.smstotelegram.activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.smstotelegram.R;
import ru.ifmo.droid2016.smstotelegram.model.User;

/**
 * Created by fox on 21.01.2017.
 */

public class ListOfUsersAdapter extends ArrayAdapter<User> {

    List<User> users = new ArrayList<>();

    public ListOfUsersAdapter(Context context, List<User> objects) {
        super(context, R.layout.user_item, objects);
        this.users = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.user_item, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView username = (TextView) convertView.findViewById(R.id.username);

        Log.e("Meow", name == null ? "name null" : "name not null");
        Log.e("Meow", username == null ? "uname null" : "uname not null");

        if (name != null && username != null) {
            name.setText(users.get(position).name);
            username.setText(users.get(position).username);
        }
        return convertView;
    }
}
