package tbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tbot.parsers.ParserMetacritic;
import tbot.service.CommandService;
import tbot.service.TextButtons;
import tbot.service.TextMessages;
import tbot.users.State;
import tbot.users.TableUsers;

import java.io.IOException;
import java.util.ArrayList;

import static tbot.BotConfig.*;

public class Bot extends TelegramLongPollingBot {
    private String chatId;
    private CommandService infoCmdService;
    private TableUsers tableUsers;


    public Bot() {
        infoCmdService = new CommandService();
        tableUsers = new TableUsers();
    }

    public String getBotUsername() {
        return USERNAME_BOT;
    }

    public String getBotToken() {
        return TOKEN_BOT;
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message msg = update.getMessage();
            if (msg.hasText()) {
                chatId = msg
                        .getChatId()
                        .toString();
                System.out.println(msg.getText());
                try {
                    execute(handleIncomingMessage(msg));
                } catch (TelegramApiException | IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private SendMessage handleIncomingMessage(Message msg) throws IOException, TelegramApiException {
        var state = tableUsers.getStateUser(chatId);
        var textMsg = msg.getText();
        SendMessage sendMsg = null;
        if (textMsg.startsWith(Commands.PREFIX)) {
            if (textMsg.startsWith(Commands.START_COMMAND)) {
                return creatMessageWithKeyboard(TextMessages.START, getStartKeyboard());
            } else if (textMsg.startsWith(Commands.STOP_COMMAND)) {
                return sendHideKeyboard();
            }
        }
        switch (state) {
            case START:
                sendMsg = messageOnStartMenu(textMsg);
                break;
            case NAME_GAME:
                sendMsg = messageOnNameGame(textMsg);
                break;
            case NAME_PLATFORM:
                sendMsg = messageOnNamePlatform(textMsg);
                break;
            default:
                sendMsg = messageDefault();
                break;
        }
        return sendMsg;
    }

    private SendMessage sendHideKeyboard() throws TelegramApiException {
        tableUsers.removeUser(chatId);
        return creatMessage(TextMessages.BYE);
    }

    private SendMessage messageOnStartMenu(String textMsg) {
        var sendMsg = new SendMessage();
        switch (textMsg) {
            case TextButtons.METACRITIC:
                sendMsg = creatMessage(TextMessages.NAME_GAME);
                tableUsers.setStateUser(chatId, State.NAME_GAME);
                break;
            default:
                sendMsg = creatMessageWithKeyboard(TextMessages.HELP, getStartKeyboard());
                tableUsers.setStateUser(chatId, State.START);
        }
        return sendMsg;
    }

    private SendMessage messageOnNameGame(String textMsg) {
        tableUsers.setStateUser(chatId, State.NAME_PLATFORM);
        tableUsers.setNameGameUser(chatId, textMsg);
        return creatMessageWithKeyboard(TextMessages.NAME_PLATFORM, getNamePlatformKeyboard());
    }

    private SendMessage messageOnNamePlatform(String textMsg) throws IOException {
        var sendMsg = new SendMessage();
        switch (textMsg) {
            case TextButtons.PLAYSTATION:
            case TextButtons.NINTENDO:
            case TextButtons.XBOX:
                tableUsers.setStateUser(chatId, State.NAME_PLATFORM);
                sendMsg = creatMessageWithKeyboard(TextMessages.SERIES, getNamePlatformSeriesKeyboard(textMsg));
                break;
            default:
                tableUsers.setPlatformUser(chatId, textMsg);
                tableUsers.setStateUser(chatId, State.START);
                sendMsg = createAnswerMessage();
                break;
        }
        return sendMsg;
    }

    private SendMessage messageDefault() {
        tableUsers.setStateUser(chatId, State.START);
        return creatMessageWithKeyboard(TextMessages.HELP, getStartKeyboard());
    }

    private SendMessage createAnswerMessage() throws IOException {
        var parserMetacritic = new ParserMetacritic();
        var answer = parserMetacritic.parseGame(
                tableUsers.getNameGameUser(chatId),
                tableUsers.getNamePlatformUser(chatId)
        );
        return creatMessageWithKeyboard(answer, getStartKeyboard());
    }

    private SendMessage creatMessageWithKeyboard(String textMsg, ArrayList<KeyboardRow> keyboard) {
        var replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return new SendMessage()
                .setChatId(chatId)
                .setText(textMsg)
                .setReplyMarkup(replyKeyboardMarkup);
    }

    private SendMessage creatMessage(String textMsg) {
        return new SendMessage()
                .setReplyMarkup(new ReplyKeyboardRemove())
                .setChatId(chatId)
                .setText(textMsg);
    }

    private ArrayList<KeyboardRow> getStartKeyboard() {
        var keyboard = new ArrayList<KeyboardRow>();
        var keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(TextButtons.METACRITIC));
        keyboard.add(keyboardFirstRow);
        return keyboard;
    }

    private ArrayList<KeyboardRow> getNamePlatformKeyboard() {
        var keyboard = new ArrayList<KeyboardRow>();
        var keyboardFirstRow = new KeyboardRow();
        var keyboardSecondRow = new KeyboardRow();
        var keyboardThirdRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(TextButtons.PLATFORM_PC));
        keyboardFirstRow.add(new KeyboardButton(TextButtons.PLAYSTATION));
        keyboardFirstRow.add(new KeyboardButton(TextButtons.XBOX));
        keyboardSecondRow.add(new KeyboardButton(TextButtons.STADIA));
        keyboardSecondRow.add(new KeyboardButton(TextButtons.ANDROID));
        keyboardSecondRow.add(new KeyboardButton(TextButtons.IOS));
        keyboardThirdRow.add(new KeyboardButton(TextButtons.NINTENDO));
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        return keyboard;
    }

    private ArrayList<KeyboardRow> getNamePlatformSeriesKeyboard(String namePlatform) {
        var keyboard = new ArrayList<KeyboardRow>();
        var keyboardFirstRow = new KeyboardRow();
        var keyboardSecondRow = new KeyboardRow();
        System.out.println("Series");
        System.out.println(namePlatform);
        switch (namePlatform) {
            case TextButtons.PLAYSTATION:
                keyboardFirstRow.add(new KeyboardButton(TextButtons.PLAYSTATION_2));
                keyboardFirstRow.add(new KeyboardButton(TextButtons.PLAYSTATION_3));
                keyboardSecondRow.add(new KeyboardButton(TextButtons.PLAYSTATION_4));
                keyboardSecondRow.add(new KeyboardButton(TextButtons.PLAYSTATION_5));
                break;
            case TextButtons.XBOX:
                keyboardFirstRow.add(new KeyboardButton(TextButtons.XBOX_SERIES_X));
                keyboardFirstRow.add(new KeyboardButton(TextButtons.XBOX_SERIES_S));
                keyboardSecondRow.add(new KeyboardButton(TextButtons.XBOX_ONE));
                keyboardSecondRow.add(new KeyboardButton(TextButtons.XBOX_360));
                break;
            case TextButtons.NINTENDO:
                keyboardFirstRow.add(new KeyboardButton(TextButtons.NINTENDO_SWITCH));
                keyboardFirstRow.add(new KeyboardButton(TextButtons.NINTENDO_WII_U));
                keyboardFirstRow.add(new KeyboardButton(TextButtons.NINTENDO_WII));
                keyboardFirstRow.add(new KeyboardButton(TextButtons.NINTENDO_3DS));
                keyboardSecondRow.add(new KeyboardButton(TextButtons.NINTENDO_DS));
                keyboardSecondRow.add(new KeyboardButton(TextButtons.NINTENDO_GAMEBOY_ADVANCE));
                keyboardSecondRow.add(new KeyboardButton(TextButtons.NINTENDO_GAMECUBE));
                break;
        }
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        return keyboard;
    }
}