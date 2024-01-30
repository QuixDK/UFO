package com.quixdk.UFOBot.handler.messages;

import com.quixdk.UFOBot.handler.buttons.MessageButtons;
import com.quixdk.UFOBot.model.Users;
import com.quixdk.UFOBot.model.UsersData;
import com.quixdk.UFOBot.service.UsersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Slf4j
@Component
@AllArgsConstructor
public class Messages {

    private final UsersService usersService;
    private final MessageButtons messageButtons;

    public SendMessage sendStartMessage(Users users) {
        return messageButtons.set(users, "<b> С чего начнем сегодня? </b>");
    }

    public SendMessage sendPaymentBill(Users users, String url) {
        String answer = "<b> Ваша ссылка на оплату: </b>\nСсылка: " + url;
        return messageButtons.set(users, answer);
    }

    public SendMessage sendPaymentMessage(Users users) {
        String answer = "<b>Введите сумму для пополнения: </b>";
        return messageButtons.set(users, answer);
    }

    public SendMessage sendErrorMessage(Users users) {
        String answer = "<b> Такой команды нет </b>";
        return messageButtons.set(users, answer);
    }

    public SendMessage sendProfileInfo(Users users) {
        UsersData usersData = usersService.findUsersDataByChatId(users.getChatId());
        StringBuilder sb = new StringBuilder();
        sb.append("<b>Ваше имя: </b>").append(usersData.getUserFirstName()).append("\n").append(
                "\n").append("<b>Ваш chatID: </b>").append(users.getChatId()).append("\n").append(
                "<b>Ваш баланс: </b>").append(usersData.getUserBalance()).append(" <b> рублей</b> ");
        return messageButtons.set(users, sb.toString());

    }

    public SendMessage sendBotInfo(Users users) {
        String answer = "<b> Мы мультивалютная крипто-биржа UFO, предназначенная для обмена и хранения криптовалюты</b>";
        return messageButtons.set(users, answer);
    }

    public SendMessage sendBotLicense(Users users) {
        String answer = "<b>Лицензии нет :(</b>";
        return messageButtons.set(users, answer);
    }

    public SendMessage startTrade(Users users) {
        String answer = "<b> Пожалуйста, выберите биржу для торговли </b>";
        return messageButtons.set(users, answer);
    }

    public EditMessageText TradeBinance(long chatID, EditMessageText editMessageText, long messageID) {
        String answerBinance = "<b>Биржа Binance успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerBinance);
        editMessageText.setParseMode("HTML");
        log.info("Message: " + answerBinance + " send to " + chatID);
        return editMessageText;
    }

    public EditMessageText TradeByBit(long chatID, EditMessageText editMessageText, long messageID) {
        String answerBybit = "<b>Биржа ByBit успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerBybit);
        editMessageText.setParseMode("HTML");
        log.info("Message: " + answerBybit + " send to " + chatID);
        return editMessageText;

    }

    public EditMessageText TradeYoBit(long chatID, EditMessageText editMessageText, long messageID) {
        String answerYobit = "<b>Биржа YoBit успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerYobit);
        editMessageText.setParseMode("HTML");
        log.info("Message: " + answerYobit + " send to " + chatID);
        return editMessageText;

    }
}
