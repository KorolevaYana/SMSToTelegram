package ru.ifmo.droid2016.smstotelegram;

import com.pengrad.telegrambot.model.Chat;

import ru.ifmo.droid2016.smstotelegram.bot.SMSToTelegramBot;
import ru.ifmo.droid2016.smstotelegram.service.SMSReciever;

/**
 * Created by fox on 21.01.2017.
 */

public class Variables {
    public static SMSToTelegramBot bot;
    public static Chat chat;
    public static SMSReciever receiver = null;
    public static boolean receiverRegistered = false;

    Variables(){}
}
