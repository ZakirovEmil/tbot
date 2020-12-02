package tbot.users;

import java.util.HashMap;

//Таблица состояний бота для пользователей

public class TableUsers {

    private HashMap<String, User> tableUsers;

    public TableUsers(){
        tableUsers = new HashMap<>();
    }

    public void setStateUser(String chatId, State state){
        var user = tableUsers.getOrDefault(chatId, new User());
        user.setState(state);
        tableUsers.put(chatId, user);
    }

    public State getStateUser(String chatId){
        return tableUsers
                .getOrDefault(chatId, new User())
                .getState();
    }

    public String getNameGameUser(String chatId){
        return tableUsers
                .get(chatId)
                .getNameGame();
    }
    public String getNamePlatformUser(String chatId){
        return tableUsers
                .get(chatId)
                .getNamePlatform();
    }

    public void setNameGameUser(String chatId, String nameGame){
        var user = tableUsers.get(chatId);
        user.setNameGame(nameGame);
        tableUsers.put(chatId, user);
    }

    public void setPlatformUser(String chatId, String namePlatform){
        var user = tableUsers.get(chatId);
        user.setNamePlatform(namePlatform);
        tableUsers.put(chatId, user);
    }

    public User getUser(String chatId){
        return tableUsers.getOrDefault(chatId, new User());
    }

    public void removeUser(String chatId){
        tableUsers.remove(chatId);
    }
}
