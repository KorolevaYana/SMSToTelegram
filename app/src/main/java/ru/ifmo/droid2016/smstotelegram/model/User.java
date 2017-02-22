package ru.ifmo.droid2016.smstotelegram.model;

/**
 * Created by fox on 21.01.2017.
 */

public class User {
    public String username = "";
    public String name = "";

    public User(String name, String lastName, String username, long id) {
        this.name = name + " " + lastName;
        this.username = username;
    }
}
