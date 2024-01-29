package com.example.WebUFO.controller.callback;

import com.example.WebUFO.controller.UserStates;
import com.example.WebUFO.handler.messages.Messages;
import com.example.WebUFO.model.Users;
import com.example.WebUFO.repository.UsersEntityRepository;
import com.example.WebUFO.service.UsersServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CallBack {

    private final UsersEntityRepository usersEntityRepository;
    private final UsersServiceImpl usersService;
    private final Messages messages;


    public BotApiMethod<? extends Serializable> send(Update update) {
        Long chatID = update.getCallbackQuery().getMessage().getChatId();
        String callbackQueryMessage = update.getCallbackQuery().getData();

        Users user = usersEntityRepository.findUserByChatId(chatID);
        if (user == null) {
            return null;
        }

        switch (user.getUserState()) {
            case StateMenu -> {
                return menuStateHandle(callbackQueryMessage, user);
            }
            case StateTrade -> {
                return tradeStateHandle(update, user);
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
        return null;
    }

    private SendMessage menuStateHandle(String CallbackQueryMessage, Users users) {
        switch (CallbackQueryMessage) {
            case "Profile" -> {
                return messages.sendProfileInfo(users);
            }
            case "BotInfo" -> {
                return messages.sendBotInfo(users);
            }
            case "License" -> {
                return messages.sendBotLicense(users);
            }
            case "StartTrade" -> {
                usersService.updateUserState(users, UserStates.StateTrade);
                return messages.startTrade(users);
            }
            case "replenishBalance" -> {
                users.setUserState(UserStates.StatePayment);
                usersService.updateUserState(users, UserStates.StatePayment);
                return messages.sendPaymentMessage(users);
            }
        }
        return null;
    }

    private EditMessageText tradeStateHandle(Update update, Users user) {
        String CallbackQueryMessage = update.getCallbackQuery().getData();
        EditMessageText editMessageText = new EditMessageText();
        Integer messageID = update.getCallbackQuery().getMessage().getMessageId();
        Long chatID = user.getChatId();
        switch (CallbackQueryMessage) {
            case "Binance" -> {
                usersService.updateUserState(user, UserStates.StateTradeBinance);
                return messages.TradeBinance(chatID, editMessageText, messageID);
            }
            case "ByBit" -> {
                usersService.updateUserState(user, UserStates.StateTradeByBit);
                return messages.TradeByBit(chatID, editMessageText, messageID);
            }
            case "YoBit" -> {
                usersService.updateUserState(user, UserStates.StateTradeYoBit);
                return messages.TradeYoBit(chatID, editMessageText, messageID);
            }
        }
        return null;
    }
}
