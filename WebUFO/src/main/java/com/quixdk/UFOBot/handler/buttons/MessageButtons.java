package com.quixdk.UFOBot.handler.buttons;

import com.quixdk.UFOBot.model.UserStates;
import com.quixdk.UFOBot.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MessageButtons {

    public SendMessage set(Users user, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode("HTML");
        sendMessage.setText(text);
        sendMessage.setChatId(user.getChatId());
        setMenuButton(sendMessage);
        //UsersBills usersBills = user.getUsersBills();

            if (user.getUserState() == UserStates.StateMenu) {
                try {
                    log.info("Message: " + text + " send to " + user.getChatId());
                    return setInlineMenuButtons(sendMessage);

                } catch (Exception e) {
                    log.error("Error occurred: " + e.getMessage());
                }
            } else if (user.getUserState() == UserStates.StateTrade) {
                try {
                    log.info("Message: " + text + " send to " + user.getChatId());
                    return setTradeInlineButtons(sendMessage);

                } catch (Exception e) {
                    log.error("Error occurred: " + e.getMessage());
                }
            } else if (user.getUserState() == UserStates.StatePayment) {
                try {
                    log.info("Message: " + text + " send to " + user.getChatId());
                    return setPayButtons(sendMessage, "null"); //usersBills.getPayUrl()));

                } catch (Exception e) {
                    log.error("Error occurred: " + e.getMessage());
                }
            } else
                try {
                    log.info("Message: " + text + " send to " + user.getChatId());
                    return sendMessage;

                } catch (Exception e) {
                    log.error("Error occurred: " + e.getMessage());
                }
        return sendMessage;
    }
    private SendMessage setInlineMenuButtons(SendMessage sendMessage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        inlineKeyboardButton1.setText("Профиль");
        inlineKeyboardButton1.setCallbackData("Profile");
        inlineKeyboardButton2.setText("Информация о боте");
        inlineKeyboardButton2.setCallbackData("BotInfo");
        inlineKeyboardButton3.setText("Лицензия");
        inlineKeyboardButton3.setCallbackData("License");
        inlineKeyboardButton4.setText("Торговать [ALPHA]");
        inlineKeyboardButton4.setCallbackData("StartTrade");
        inlineKeyboardButton5.setText("Пополнить баланс [ALPHA]");
        inlineKeyboardButton5.setCallbackData("replenishBalance");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow2.add(inlineKeyboardButton3);
        keyboardButtonsRow2.add(inlineKeyboardButton4);
        keyboardButtonsRow3.add(inlineKeyboardButton5);
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(keyboardButtonsRow1);
        rowsInline.add(keyboardButtonsRow2);
        rowsInline.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    private SendMessage setTradeInlineButtons(SendMessage sendMessage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        inlineKeyboardButton1.setText("Binance");
        inlineKeyboardButton1.setCallbackData("Binance");
        inlineKeyboardButton2.setText("ByBit");
        inlineKeyboardButton2.setCallbackData("ByBit");
        inlineKeyboardButton3.setText("YoBit");
        inlineKeyboardButton3.setCallbackData("YoBit");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow1.add(inlineKeyboardButton3);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    private SendMessage setPayButtons(SendMessage sendMessage, String payUrl) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        inlineKeyboardButton1.setText("Оплатить");
        inlineKeyboardButton1.setCallbackData("Pay");
        inlineKeyboardButton1.setUrl(payUrl);
        inlineKeyboardButton2.setText("Проверить оплату");
        inlineKeyboardButton2.setCallbackData("CheckPay");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    private void setMenuButton(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Меню"));


        keyboardRowList.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
}
