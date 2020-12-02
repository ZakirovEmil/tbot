package tbot.service;

//Получение информации о коммандах

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CommandService {

    private static final String PATH_TO_TEXT_COMMAND = "src/main/resources/TextCommands.properties";
    private Properties prop;

    public CommandService(){
        FileReader fileInput;
        //инициализируем специальный объект Properties
        //типа Hashtable для удобной работы с данными
        prop = new Properties();

        try {
            //обращаемся к файлу и получаем данные
            fileInput = new FileReader(PATH_TO_TEXT_COMMAND);
            prop.load(fileInput);
        } catch (IOException e) {
            System.out.println("Ошибка в программе: файл " + PATH_TO_TEXT_COMMAND + " не обнаружено");
            e.printStackTrace();
        }
    }

    public String getInfoCommand(String nameProp){
        return prop.getProperty(nameProp);
    }
}
