package tbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tbot.parsers.ParserMetacritic;
import tbot.service.CommandService;
import tbot.users.State;
import tbot.users.TableUsers;

import java.io.IOException;

import static tbot.Commands.*;
import static tbot.BotConfig.*;

public class Bot extends TelegramLongPollingBot {
    private String chatId;
    private CommandService infoCmdService;
    private TableUsers tableUsers;

    public Bot(){
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
        if(update.hasMessage()){
            Message msg = update.getMessage();
            if(msg.hasText()){
                chatId = msg
                        .getChatId()
                        .toString();
                try {
                    execute(handleIncomingMessage(msg));
                } catch (TelegramApiException | IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private SendMessage handleIncomingMessage(Message msg) throws IOException {
        var textMsg = msg
                .getText()
                .toLowerCase();
        var cmdMsg = textMsg
                .trim()
                .split(" ")[0];
        SendMessage sendMsg = null;
        if (cmdMsg.startsWith(PREFIX)){
            sendMsg = handlerCommand(cmdMsg);
        } else {
            sendMsg = anotherCommand(textMsg);
        }
        if (sendMsg == null){
            throw new ArithmeticException();
        }
        return sendMsg;
    }

    private SendMessage handlerCommand(String cmdMsg) {
        SendMessage sendMsg = null;
        switch (cmdMsg) {
            case START_COMMAND:
                tableUsers.setStateUser(chatId, State.START);
                sendMsg = creatSendMessage("startCommand");
                break;
            case HELP_COMMAND:
                tableUsers.setStateUser(chatId, State.START);
                sendMsg = creatSendMessage("helpCommand");
                break;
            case SEARCH_GAME_COMMAND:
                tableUsers.setStateUser(chatId, State.NAME_GAME);
                sendMsg = creatSendMessage("nameGame");
                break;
            default:
                sendMsg = creatSendMessage("notCommand");
        }
        return sendMsg;
    }

    private SendMessage anotherCommand(String msg) throws IOException {
        SendMessage sendMsg = null;
        var stateUser= tableUsers.getStateUser(chatId);
        switch (stateUser) {
            case START:
                sendMsg = creatSendMessage("helpCommand");
                break;
            case NAME_GAME:
                tableUsers.setNameGameUser(chatId, msg);
                tableUsers.setStateUser(chatId, State.NAME_PLATFORM);
                sendMsg = creatSendMessage("namePlatform");
                break;
            case NAME_PLATFORM:
                tableUsers.setPlatformUser(chatId, msg);
                tableUsers.setStateUser(chatId, State.START);
                sendMsg = createAnswerSendMessage();
//                sendMsg = new SendMessage()
//                        .setChatId(chatId)
//                        .setText("User:"  +
//                                tableUsers.getNameGameUser(chatId) +
//                                " " +
//                                tableUsers.getNamePlatformUser(chatId));
                break;
        }
        return sendMsg;
    }

    private SendMessage createAnswerSendMessage() throws IOException {
        var parserMetacritic = new ParserMetacritic(tableUsers.getNameGameUser(chatId),
                                    tableUsers.getNamePlatformUser(chatId));
        var answer = parserMetacritic.getInfoMetacritic();
        return new SendMessage()
                .setChatId(chatId)
                .setText(answer);
    }

    private SendMessage creatSendMessage(String textMsg){
        return new SendMessage()
                .setChatId(chatId)
                .setText(infoCmdService.getInfoCommand(textMsg));
    }
}