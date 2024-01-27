package com.example.WebUFO.handler.messages;

import com.example.WebUFO.controller.UFO;
import com.example.WebUFO.controller.callback.Message;
import com.example.WebUFO.model.Users;
import com.example.WebUFO.model.UsersData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Slf4j
public class Messages {

    @Autowired
    UFO ufo;
    Message message = new Message();

    public void sendStartMessage(Users users) {
        message.send(users, "<b> С чего начнем сегодня? </b>");
    }

    public void sendPaymentBill(Users users, String url) {
        String answer = "<b> Ваша ссылка на оплату: \n</b>" + "Ссылка: " + url;
        message.send(users, answer);
    }

    public void sendPaymentMessage(Users users) {
        String answer = "<b>Введите сумму для пополнения: </b>";
        message.send(users, answer);
    }

    public void sendErrorMessage(Users users) {
        String answer = "<b> Такой команды нет </b>";
        message.send(users, answer);
    }

    public void sendProfileInfo(Users users) {
        UsersData usersData = users.getUsersData();

        String answer = "<b>Ваше имя: </b>" + usersData.getUserFirstName() + " \n"
                + "<b>Ваш chatID: </b>" + users.getUserChatID() + " \n"
                + "<b>Ваш баланс: </b>" + usersData.getUserBalance() + " <b> рублей</b> ";
        message.send(users, answer);

    }

    public void sendBotInfo(Users users) {
        String answer = "<b> Мы мультивалютная крипто-биржа UFO, предназначенная для обмена и хранения криптовалюты</b>";
        message.send(users, answer);
    }

    public void sendBotLicense(Users users) {
        String answer = "<b>Лицензии нет :(</b>";
        message.send(users, answer);
    }

    public void startTrade(Users users) {
        String answer = "<b> Пожалуйста, выберите биржу для торговли </b>";
        message.send(users, answer);
    }

    public void TradeBinance(long chatID, EditMessageText editMessageText, long messageID) {
        String answerBinance = "<b>Биржа Binance успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerBinance);
        editMessageText.setParseMode("HTML");
        try {
            ufo.execute(editMessageText);
            log.info("Message: " + answerBinance + " send to " + chatID);
        } catch (Exception e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    public void TradeByBit(long chatID, EditMessageText editMessageText, long messageID) {
        String answerBybit = "<b>Биржа ByBit успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerBybit);
        editMessageText.setParseMode("HTML");
        try {
            ufo.execute(editMessageText);
            log.info("Message: " + answerBybit + " send to " + chatID);
        } catch (Exception e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    public void TradeYoBit(long chatID, EditMessageText editMessageText, long messageID) {
        String answerYobit = "<b>Биржа YoBit успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerYobit);
        editMessageText.setParseMode("HTML");
        try {
            ufo.execute(editMessageText);
            log.info("Message: " + answerYobit + " send to " + chatID);
        } catch (Exception e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
