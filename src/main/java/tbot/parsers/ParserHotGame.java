package tbot.parsers;


import org.javatuples.Pair;
import org.jsoup.Jsoup;
import tbot.service.TextButtons;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class ParserHotGame extends Parser{

    private static final HashMap<String, String> websiteKey = new HashMap<String, String>();
    private static final String errorTag = "module errorcode_module error404_module";

    public ParserHotGame(){
        websiteKey.put("Steam", "[onclick*=store.steampowered.com]");
        websiteKey.put("Origin", "[onclick*=store.origin.com]");
        websiteKey.put("Uplay", "[onclick*=store.ubi.com]");
        websiteKey.put("Gog", "[onclick*=store.gog.com]");
        websiteKey.put("EGS", "[onclick*=store.epicgames.com]");
    }

    public static String parseGame(String nameGame, String namePlatform) throws IOException {
        System.out.println(namePlatform + "!!!!!!!!!!!!!!!!!!!!");
// if (namePlatform != TextButtons.PLATFORM_PC)
// return "";
        var link = creatLinkHotGames(nameGame);
        if (!linkIsCorrect(link))
            return "";
        var document = connect(link);
        return getPrice(document, namePlatform);
    }

    private static String creatLinkHotGames(String nameGame) {
        String LINK_HOT_GAME = "https://hot-game.info/game/";
        var link = LINK_HOT_GAME + nameGame.replace(" ", "-");
        System.out.println(link);
        return link;
    }

    private static String getPrice(Document document, String namePlatform) {
        Elements rawString = null;
        try {
            rawString = document.getElementsByClass("game-prices");
        }catch (Exception e){
            e.printStackTrace();
        }
        return magSearch(rawString);
    }

    private static String magSearch(Elements gamePriceList){
        var priceCurrent = "";
        for (Map.Entry<String,String> i : websiteKey.entrySet()){
            System.out.println(i.getKey() + i.getValue());
            var price = gamePriceList.select(i.getValue()).text();
            if (!price.isEmpty())
                priceCurrent = priceCurrent + "\n" +
                        i.getKey() + ":" + price;
        }
        System.out.println(priceCurrent);
        return priceCurrent;
    }
}