package org.example.settingsFORkeyboard;


import lombok.Data;
import org.example.bot.CurrencyTelegramBot;
import org.example.model.UserSettings;
import org.example.utils.ConstantData;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;
import java.util.List;


@Data
public class SettingsForKeyboard {


    private final CurrencyTelegramBot bot;
    private UserSettings.ChoiceBank selectedBank;
    private UserSettings.Currency selectedCurrency;
    private UserSettings.DecimalPlaces decimalPlaces;
    private String notificationTime;

    public SettingsForKeyboard(CurrencyTelegramBot bot) {
        this.bot = bot;
    }

    public void updateSettings(String chatId, UserSettings newSettings) {

        this.selectedBank = newSettings.getBanks();
        this.selectedCurrency = newSettings.getCurrency();
        this.decimalPlaces = newSettings.getDecimalPlaces();
        this.notificationTime = newSettings.getNotificationTime();
        try {
            StringBuilder messageText = new StringBuilder();
            messageText.append("Updated settings:\n\n")
                    .append("Selected bank: ").append(bot.getBankService().getHashSetBank()).append("\n")
                    .append("Selected currency: ").append(bot.getCurrencyService().getHashSetCurrencies()).append("\n")
                    .append("Decimal places: ").append(decimalPlaces).append("\n")
                    .append("Notification time: ").append(notificationTime).append("\n\n")
                    .append("If the selected time has already passed,\n the message will arrive tomorrow.\n" +
                            "To change the time,\n push the off and try again. ");

            SendMessage message = new SendMessage();
            message.setText(messageText.toString());
            message.setChatId(chatId);
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();

        }

    }

    public String sendExchangeRates(String chatId) {
        StringBuilder messageText = new StringBuilder();
        try {
            messageText = bot.getBankService().getFinalStringBuilder();
            SendMessage message = new SendMessage();
            message.setText(messageText.toString());
            message.setChatId(chatId);
            bot.execute(message);
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();

        }
        return messageText.toString();
    }

    public void sendSettingsMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Menu");
        message.setReplyMarkup(createSettingsMenuKeyboard());
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private ReplyKeyboardMarkup createCurrencyKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add("USD");
        row1.add("EUR");
        row2.add("Remove USD");
        row2.add("Remove EUR");
        row3.add("Settings Menu");
        row3.add("Get Info");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }


    public void sendNotificationTimeSettings(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Select notification time");
        message.setReplyMarkup(createNotificationTimeKeyboard());
        try {
            bot.execute(message);

            bot.getSendMessageOnTime().sendMessageByTime(Integer.parseInt(text), Long.valueOf(chatId));
        } catch (SchedulerException | TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup createNotificationTimeKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Додавання кнопок для вибору часу нотифікації від 9 до 18 годин
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        KeyboardRow row5 = new KeyboardRow();
        row1.add("9");
        row1.add("10");
        row1.add("11");
        row2.add("12");
        row2.add("13");
        row2.add("14");
        row3.add("15");
        row3.add("16");
        row3.add("17");
        row4.add("18");
        row4.add("off");
        row5.add("Settings Menu");
        row5.add("Get Info");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }


    public void sendSignAfterCommaSettings(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select the number of decimal places");
        message.setReplyMarkup(createSignAfterCommaKeyboard());

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendCurrencySettings(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select the currency");
        message.setReplyMarkup(createCurrencyKeyboard());
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendBankSettings(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select the bank");
        message.setReplyMarkup(createBankKeyboard());

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup createBankKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add("NBU");
        row1.add("PrivatBank");
        row1.add("MonoBank");
        row2.add("Remove NBU");
        row2.add("Remove PrivatBank");
        row2.add("Remove MonoBank");
        row3.add("Settings Menu");
        row3.add(ConstantData.GET_INFO);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup createSettingsMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        row1.add(ConstantData.DECIMAL_PLACES);
        row2.add(ConstantData.BANK);
        row2.add(ConstantData.CURRENCY);
        row3.add(ConstantData.NOTIFICATION_TIME);
        row4.add(ConstantData.SETTNGS);
        row4.add(ConstantData.GET_INFO);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup createSignAfterCommaKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("2");
        row1.add("3");
        row1.add("4");
        row2.add(ConstantData.SETTNGS);
        row2.add(ConstantData.GET_INFO);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}





