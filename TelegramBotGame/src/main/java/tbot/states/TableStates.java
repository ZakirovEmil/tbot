package tbot.states;

import java.util.HashMap;

//Таблица состояний бота для пользователей

public class TableStates {

    private HashMap<String, States> tableStates;

    public TableStates(){
        tableStates = new HashMap<>();
    }

    public void SetUserState(String chatId, States states){
        tableStates.put(chatId, states);
    }

}
