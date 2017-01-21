package ru.ifmo.droid2016.smstotelegram.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SetWebhook;

/**
 * Created by Koroleva Yana.
 */

public class SMSToTelegramBot {
    public TelegramBot bot;

    public SMSToTelegramBot() {
        this.bot = TelegramBotAdapter.build("262208833:AAHGT1IQLktkCoGKJxnc0dMGmQm5KAidbws"); //bot API token example
    }


}
