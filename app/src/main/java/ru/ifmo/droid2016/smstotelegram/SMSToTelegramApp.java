package ru.ifmo.droid2016.smstotelegram;

import android.app.Application;
import android.content.Intent;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Chat;

import ru.ifmo.droid2016.smstotelegram.service.SMSReceiver;

/**
 * Created by fox on 22.01.2017.
 */

public class SMSToTelegramApp extends Application {
    //example token
    // private final static String TOKEN = "262208833:AAGmigAP8z0tijtdwr6iI5xK1mrbDzNhejM";
    private final static String TOKEN = "262208833:AAEhim_mtARf46GC4nwZp9KtXZup9nTUFMw";

    public static SMSReceiver receiver;
    public static /*volatile */Chat chat;
    public static TelegramBot bot = null;
    public static Intent intent = null;

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new SMSReceiver();
        bot = TelegramBotAdapter.build(TOKEN);
        chat = null;
    }
}
