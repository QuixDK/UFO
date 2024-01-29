package com.example.WebUFO.handler.messages;

import com.example.WebUFO.controller.UFO;
import com.example.WebUFO.controller.callback.Message;
import com.example.WebUFO.model.Users;
import com.example.WebUFO.model.UsersData;
import com.example.WebUFO.service.UsersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Slf4j
@Component
@AllArgsConstructor
public class Messages {

    private final UsersService usersService;
    private final Message message;

    public SendMessage sendStartMessage(Users users) {
        return message.send(users, "<b> С чего начнем сегодня? </b>");
    }

    public SendMessage sendPaymentBill(Users users, String url) {
        String answer = "<b> Ваша ссылка на оплату: \n</b>" + "Ссылка: " + url;
        return message.send(users, answer);
    }

    public SendMessage sendPaymentMessage(Users users) {
        String answer = "<b>Введите сумму для пополнения: </b>";
        return message.send(users, answer);
    }

    public SendMessage sendErrorMessage(Users users) {
        String answer = "<b> Такой команды нет </b>";
        return message.send(users, answer);
    }

    public SendMessage sendProfileInfo(Users users) {
        UsersData usersData = usersService.findUsersDataByChatId(users.getChatId());
        String answer = "<b>Ваше имя: </b>" + usersData.getUserFirstName() + " \n"
                + "<b>Ваш chatID: </b>" + users.getChatId() + " \n"
                + "<b>Ваш баланс: </b>" + usersData.getUserBalance() + " <b> рублей</b> ";
        return message.send(users, answer);

    }

    public SendMessage sendBotInfo(Users users) {
        String answer = "<b> Мы мультивалютная крипто-биржа UFO, предназначенная для обмена и хранения криптовалюты</b>";
        return message.send(users, answer);
    }

    public SendMessage sendBotLicense(Users users) {
        String answer = "<b>Лицензии нет :(</b>";
        return message.send(users, answer);
    }

    public SendMessage startTrade(Users users) {
        String answer = "<b> Пожалуйста, выберите биржу для торговли </b>";
        return message.send(users, answer);
    }

    public EditMessageText TradeBinance(long chatID, EditMessageText editMessageText, long messageID) {
        String answerBinance = "<b>Биржа Binance успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerBinance);
        editMessageText.setParseMode("HTML");
        try {
            log.info("Message: " + answerBinance + " send to " + chatID);
            return editMessageText;

        } catch (Exception e) {
            log.error("Error occurred: " + e.getMessage());
            return editMessageText;
        }
    }

    public EditMessageText TradeByBit(long chatID, EditMessageText editMessageText, long messageID) {
        String answerBybit = "<b>Биржа ByBit успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerBybit);
        editMessageText.setParseMode("HTML");
        try {
            log.info("Message: " + answerBybit + " send to " + chatID);
            return editMessageText;
        } catch (Exception e) {
            log.error("Error occurred: " + e.getMessage());
            return editMessageText;
        }
    }

    public EditMessageText TradeYoBit(long chatID, EditMessageText editMessageText, long messageID) {
        String answerYobit = "<b>Биржа YoBit успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerYobit);
        editMessageText.setParseMode("HTML");
        try {
            log.info("Message: " + answerYobit + " send to " + chatID);
            return editMessageText;
        } catch (Exception e) {
            log.error("Error occurred: " + e.getMessage());
            return editMessageText;
        }
    }
}
