package com.example.WebUFO.controller.callback;

import com.example.WebUFO.controller.UserStates;
import com.example.WebUFO.handler.messages.Messages;
import com.example.WebUFO.model.Users;
import com.example.WebUFO.repository.UsersEntityRepository;
import com.example.WebUFO.service.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class CallBack {

    @Autowired
    UsersEntityRepository usersEntityRepository;

    @Autowired
    UsersServiceImpl usersService;

    Messages messages = new Messages();

    public void send(Update update) {
        Long chatID = update.getCallbackQuery().getMessage().getChatId();
        String callbackQueryMessage = update.getCallbackQuery().getData();

        Users user = usersEntityRepository.findUserById(chatID);
        if (user == null) {
            return;
        }

        switch (user.getUserState()) {
            case StateMenu -> {
                menuStateHandle(callbackQueryMessage, user);
            }
            case StateTrade -> {
                tradeStateHandle(update, user);
            }
            case StatePayment -> {

            }
            case StateProcessPayment -> {
                switch (callbackQueryMessage) {
                    case "Pay" -> {

                    }
                    case "CheckPay" -> {
                        //checkpay function
                    }
                }
            }

        }
    }

    private void menuStateHandle(String CallbackQueryMessage, Users users) {
        switch (CallbackQueryMessage) {
            case "Profile" -> messages.sendProfileInfo(users);
            case "BotInfo" -> messages.sendBotInfo(users);
            case "License" -> messages.sendBotLicense(users);
            case "StartTrade" -> {
                usersService.updateUserState(users, UserStates.StateTrade);
                messages.startTrade(users);
            }
            case "replenishBalance" -> {
                users.setUserState(UserStates.StatePayment);
                usersService.updateUserState(users, UserStates.StatePayment);
                messages.sendPaymentMessage(users);
            }
        }
    }

    private void tradeStateHandle(Update update, Users user) {
        String CallbackQueryMessage = update.getCallbackQuery().getData();
        EditMessageText editMessageText = new EditMessageText();
        Integer messageID = update.getCallbackQuery().getMessage().getMessageId();
        Long chatID = user.getUserChatID();
        switch (CallbackQueryMessage) {
            case "Binance" -> {
                usersService.updateUserState(user, UserStates.StateTradeBinance);
                messages.TradeBinance(chatID, editMessageText, messageID);
            }
            case "ByBit" -> {
                usersService.updateUserState(user, UserStates.StateTradeByBit);
                messages.TradeByBit(chatID, editMessageText, messageID);
            }
            case "YoBit" -> {
                usersService.updateUserState(user, UserStates.StateTradeYoBit);
                messages.TradeYoBit(chatID, editMessageText, messageID);
            }
        }
    }
}
