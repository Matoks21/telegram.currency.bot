package org.example.notification;


import lombok.Data;
import org.example.bot.CurrencyTelegramBot;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Data
public class Message implements Job {

    private CurrencyTelegramBot currencyTelegramBot;

    public Message() {
        this.currencyTelegramBot = new CurrencyTelegramBot();
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        SendMessage sendMessage = new SendMessage();
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Long chatId = jobDataMap.getLong("chatId");
        String rate = jobDataMap.getString("rate");

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(rate);


        try {
            currencyTelegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
