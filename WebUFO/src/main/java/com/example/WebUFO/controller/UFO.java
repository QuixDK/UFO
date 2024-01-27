package com.example.WebUFO.service;


import com.example.WebUFO.config.BotConfig;
import com.example.WebUFO.model.Users;
import com.example.WebUFO.repository.UsersEntityRepository;
import com.example.WebUFO.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Component
public class UFO extends TelegramLongPollingBot {

    private final BotConfig config;
    private final UsersEntityRepository usersEntityRepository;

    private final String SendStartState = "UPDATE users_data_table set user_state = 0 where chatid = ";
    private final String SendMenuState = "UPDATE users_data_table set user_state = 1 where chatid = ";
    private final String SendPaymentState = "UPDATE users_data_table set user_state = 2 where chatid = ";
    private final String SendTradeState = "UPDATE users_data_table set user_state = 3 where chatid = ";
    private final String SendTradeBinanceState = "UPDATE users_data_table set user_state = 4 where chatid = ";
    private final String SendTradeByBitState = "UPDATE users_data_table set user_state = 5 where chatid = ";
    private final String SendTradeYoBitState = "UPDATE users_data_table set user_state = 6 where chatid = ";
    private final String PaymentProcess = "UPDATE users_data_table set user_state = 7 where chatid = ";
    private final String SetBillID = "UPDATE users_data_table set billid = ";
    private final String SetPayURL = "UPDATE users_data_table set pay_url = ";

    @Autowired
    public UFO(BotConfig config, UsersEntityRepository usersEntityRepository) {
        this.config = config;
        this.usersEntityRepository = usersEntityRepository;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        boolean updateHasMessage = update.hasMessage() && update.getMessage().hasText();
        boolean updateHasCallBackQuery = update.hasCallbackQuery();
        if (updateHasMessage) {
            String textMessage = update.getMessage().getText();
            long chatID = update.getMessage().getChatId();
            String firstName = update.getMessage().getChat().getFirstName();
            String secondName = update.getMessage().getChat().getLastName();
            String userName = update.getMessage().getChat().getUserName();
            createNewUser(chatID,firstName,secondName,userName);
            sendAnswer(chatID, firstName, textMessage, secondName, userName);

        }
        else if (updateHasCallBackQuery) {
            String CallbackQueryMessage = update.getCallbackQuery().getData();
            long chatID = update.getCallbackQuery().getMessage().getChatId();
            long messageID = update.getCallbackQuery().getMessage().getMessageId();
            EditMessageText editMessageText = new EditMessageText();
            var user = userRepository.findById(chatID);
            user.ifPresent(value -> sendCallBackAnswer(chatID, messageID, CallbackQueryMessage, editMessageText));
        }
    }

    private void sendCallBackAnswer(long chatID, long messageID, String CallbackQueryMessage, EditMessageText editMessageText) {
        var user = userRepository.findById(chatID);
        if (user.isPresent()) {
            switch (user.get().getUserState()) {
                case 1 -> {
                    switch (CallbackQueryMessage) {
                        case "Profile" -> sendProfileInfo(chatID);
                        case "BotInfo" -> sendBotInfo(chatID);
                        case "License" -> sendBotLicense(chatID);
                        case "StartTrade" -> {
                            user.get().setUserState(3);
                            executeDbUpdate(SendTradeState, chatID);
                            startTrade(chatID);
                        }
                        case "replenishBalance" -> {
                            user.get().setUserState(2);
                            executeDbUpdate(SendPaymentState, chatID);
                            sendPaymentMessage(chatID);
                        }
                    }
                }
                case 3 -> {
                    switch (CallbackQueryMessage) {
                        case "Binance" -> {
                            user.get().setUserState(4);
                            executeDbUpdate(SendTradeBinanceState, chatID);
                            TradeBinance(chatID, editMessageText, messageID);
                        }
                        case "ByBit" -> {
                            user.get().setUserState(5);
                            executeDbUpdate(SendTradeByBitState, chatID);
                            TradeByBit(chatID, editMessageText, messageID);
                        }
                        case "YoBit" -> {
                            user.get().setUserState(6);
                            executeDbUpdate(SendTradeYoBitState, chatID);
                            TradeYoBit(chatID, editMessageText, messageID);
                        }
                    }
                }
                case 2 -> {

                }
                case 7 -> {
                    switch (CallbackQueryMessage) {
                        case "Pay" -> {

                        }
                        case "CheckPay" -> {
                            //checkpay function
                        }
                    }
                }
            }
        }
    }


    private void sendAnswer(long chatID, String firstName, String textMessage, String secondName, String userName) {
        Optional<Users> user = userRepository.findById(chatID);
        if (user.isPresent()) {
            if (user.get().getUserState() == 0) {
                if (textMessage.equals("/start")) {
                    catchStartCommand(chatID, firstName);
                    user.get().setUserState(1);
                    executeDbUpdate(SendMenuState, chatID);
                    sendStartMessage(chatID);
                } else {
                    sendErrorMessage(chatID);
                }
            } else if (user.get().getUserState() == 2) {
                if (isNumber(textMessage)) {
                    createBill(textMessage,chatID);
                } else {
                    if(textMessage.equals("Меню")) {
                        user.get().setUserState(1);
                        executeDbUpdate(SendMenuState, chatID);
                        sendStartMessage(chatID);
                    }
                    else
                    sendMessage(chatID, "<b>Введите число!</b>");
                }
            } else {
                if (textMessage.equals("Меню")) {
                    user.get().setUserState(1);
                    executeDbUpdate(SendMenuState, chatID);
                    sendStartMessage(chatID);
                } else {
                    sendErrorMessage(chatID);
                }
            }
        }
    }

    private boolean isNumber(String textMessage) {
        try {
            Integer.parseInt(textMessage);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }


    private void sendMessage(long chatID, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode("HTML");
        sendMessage.setText(text);
        sendMessage.setChatId(chatID);
        setMenuButton(sendMessage);
        var user = userRepository.findById(chatID);
        if (user.isPresent()) {
            if (user.get().getUserState() == 1) {
                try {
                    execute(setInlineMenuButtons(sendMessage));
                    log.info("Message: " + text + " send to " + chatID);
                } catch (Exception e) {
                    log.error("Error occurred: " + e.getMessage());
                }
            } else if (user.get().getUserState() == 3) {
                try {
                    execute(setTradeInlineButtons(sendMessage));
                    log.info("Message: " + text + " send to " + chatID);
                } catch (Exception e) {
                    log.error("Error occurred: " + e.getMessage());
                }
            } else if (user.get().getUserState() == 7) {
                try {
                    execute(setPayButtons(sendMessage, user.get().getPayUrl()));
                    log.info("Message: " + text + " send to " + chatID);
                } catch (Exception e) {
                    log.error("Error occurred: " + e.getMessage());
                }
            } else
                try {
                    execute(sendMessage);
                    log.info("Message: " + text + " send to " + chatID);
                } catch (Exception e) {
                    log.error("Error occurred: " + e.getMessage());
                }
        }
    }


    //Database

    private void createNewUser(long chatID, String firstName, String secondName, String userName) {
        if (userRepository.findById(chatID).isEmpty()) {
            Users users = new Users();

            users.setUserState(0);
            executeDbUpdate(SendStartState,chatID);
            users.setChatID(chatID);
            users.setUserName(userName);
            users.setUserFirstName(firstName);
            users.setUserSecondName(secondName);


            userRepository.save(users);
            log.info("user saved" + users);
        }
    }

    private void executeDbUpdate(String SQLCommand, long chatID) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ufo_bot", "root", "inspector"))
        {
            Statement statement = conn.createStatement();
            statement.executeUpdate(SQLCommand + chatID);
        }
        catch (Exception e) {
            log.error("Connection to DB was failed" + e);
        }
    }

    //Buttons

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

    //Messages types

    private void sendPaymentBill(long chatID, String url) {
        String answer = "<b> Ваша ссылка на оплату: \n</b>" + "Ссылка: " + url;
        sendMessage(chatID, answer);
    }

    private void sendPaymentMessage(long chatID) {
        String answer = "<b>Введите сумму для пополнения: </b>";
        sendMessage(chatID,answer);
    }

    private void sendStartMessage(long chatID) {
        sendMessage(chatID,"<b> С чего начнем сегодня? </b>");
    }

    private void catchStartCommand(long chatID, String firstName) {
        String answer = "<b> Привет, " + firstName +  ", добро пожаловать в мультивалютную крипто-биржу UFO!</b>";
        sendMessage(chatID, answer);
    }

    private void sendErrorMessage(long chatID) {
        String answer = "<b> Такой команды нет </b>";
        sendMessage(chatID,answer);
    }

    private void sendProfileInfo(long chatID) {
        var user = userRepository.findById(chatID);
        if (user.isPresent()) {
            String answer = "<b>Ваше имя: </b>" + user.get().getUserFirstName() + " \n"
                    + "<b>Ваш chatID: </b>" + user.get().getChatID() + " \n"
                    + "<b>Ваш баланс: </b>" + user.get().getUserBalance() + " <b> рублей</b> ";
            sendMessage(chatID, answer);
        }
    }

    private void sendBotInfo(long chatID) {
        String answer = "<b> Мы мультивалютная крипто-биржа UFO, предназначенная для обмена и хранения криптовалюты</b>";
        sendMessage(chatID,answer);
    }

    private void sendBotLicense(long chatID) {
        String answer = "<b>Лицензии нет :(</b>";
        sendMessage(chatID, answer);
    }

    private void startTrade(long chatID) {
        String answer = "<b> Пожалуйста, выберите биржу для торговли </b>";
        sendMessage(chatID,answer);
    }

    private void TradeBinance(long chatID, EditMessageText editMessageText, long messageID) {
        String answerBinance = "<b>Биржа Binance успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerBinance);
        editMessageText.setParseMode("HTML");
        try {
            execute(editMessageText);
            log.info("Message: " + answerBinance + " send to " + chatID);
        }
        catch (Exception e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
    private void TradeByBit(long chatID, EditMessageText editMessageText, long messageID) {
        String answerBybit = "<b>Биржа ByBit успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerBybit);
        editMessageText.setParseMode("HTML");
        try {
            execute(editMessageText);
            log.info("Message: " + answerBybit + " send to " + chatID);
        }
        catch (Exception e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
    private void TradeYoBit(long chatID, EditMessageText editMessageText, long messageID) {
        String answerYobit = "<b>Биржа YoBit успешно выбрана!</b>";
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) messageID);
        editMessageText.setText(answerYobit);
        editMessageText.setParseMode("HTML");
        try {
            execute(editMessageText);
            log.info("Message: " + answerYobit + " send to " + chatID);
        }
        catch (Exception e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
