package org.example.notification;


import lombok.Data;
import org.example.bot.CurrencyTelegramBot;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.TriggerBuilder.newTrigger;

@Data
public class SendMessageOnTime {
    private CurrencyTelegramBot bot;
    public Scheduler scheduler;
    JobDetail job;

    public SendMessageOnTime(CurrencyTelegramBot bot) {
        this.bot = bot;
    }

    public  void sendMessageByTime(int hour, Long chatId) throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        job = JobBuilder.newJob(Message.class)
                .withIdentity(String.valueOf(chatId), "daily-jobs")
                .usingJobData("chatId", chatId)
                .usingJobData("rate", bot.getSettingsForKeyboard().sendExchangeRates(String.valueOf(chatId)))
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("myTrigger", "myTriggerGroup")
                .withSchedule(dailyAtHourAndMinute(hour, 14))
                .build();
        Date date = scheduler.scheduleJob(job, trigger);


        final Map<String, JobDetail> jobDetailsMap = new HashMap<>();
        jobDetailsMap.put(String.valueOf(chatId), job);
        scheduler.start();

        System.out.println(date);
    }

    public  void deleteJob() throws SchedulerException {
        scheduler.deleteJob(job.getKey());
        scheduler.shutdown(true);
    }
}