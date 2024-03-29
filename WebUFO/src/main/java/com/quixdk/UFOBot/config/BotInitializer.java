package com.quixdk.UFOBot.config;

import com.quixdk.UFOBot.controller.MainBotController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class BotInitializer {

    private final MainBotController mainBotController;

    public BotInitializer(MainBotController mainBotController) {
        this.mainBotController = mainBotController;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(mainBotController);
        } catch (Exception e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
