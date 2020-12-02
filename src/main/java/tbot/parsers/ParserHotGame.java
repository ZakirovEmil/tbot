package tbot.parsers;


import tbot.service.TextButtons;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class ParserHotGame extends Parser {

    private static final HashMap<String, String> websiteKey = new HashMap<>();

    public static String parseGame(String nameGame, String namePlatform) throws IOException {
        fillWebsiteKey();
        System.out.println(namePlatform);
        if (!namePlatform.equals(TextButtons.PLATFORM_PC))
            return "";
        var link = creatLinkHotGames(nameGame);
        if (!linkIsCorrect(link))
            return "";
        var document = connect(link);
        return getPrice(document, namePlatform);
    }

    private static void fillWebsiteKey() {
        websiteKey.put("Steam", "div[onclick*=store.steampowered.com]");
        websiteKey.put("Origin", "div[onclick*=store.origin.com]");
        websiteKey.put("Uplay", "div[onclick*=store.ubi.com]");
        websiteKey.put("Gog", "div[onclick*=store.gog.com]");
        websiteKey.put("EGS", "div[onclick*=store.epicgames.com]");
        websiteKey.put("Mvideo", "div[onclick*=www.mvideo.ru]");
        websiteKey.put("PSstore", "div[onclick*=store.playstation.com]");
        websiteKey.put("Eldorado", "div[onclick*=www.eldorado.ru]");
    }

    private static String creatLinkHotGames(String nameGame) {
        var link = "https://hot-game.info/game/";
        var nameGameForLink = nameGame.replace(" ", "-").toLowerCase();
        var link1 = link + nameGameForLink;
        if (linkIsCorrect(link1)) {
            link = link1;
        } else {
            var output = "";
            String[] words = nameGameForLink.split("-");
            for (String word : words) {
                String first = word.substring(0, 1).toUpperCase();
                String all = word.substring(1);
                output += first + all + " ";
            }
            System.out.println(output);
            link = link + output.trim().replace(" ", "-");
        }
        System.out.println(link);
        return link;
    }

    private static String getPrice(Document document, String namePlatform) {
        Elements rawString = null;
        try {
            rawString = document.getElementsByClass("game-prices");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return magSearch(rawString);
    }

    private static String magSearch(Elements gamePriceList) {
        var priceCurrent = "";
        for (Map.Entry<String, String> i : websiteKey.entrySet()) {
            System.out.println(i.getKey() + i.getValue());
            String price;
            var priceList = gamePriceList.select(i.getValue()).text().split(" ");
            if (priceList.length > 2)
                price = priceList[0].indexOf("%") == -1 ?
                        priceList[0] :
                        priceList[1] + priceList[2] + "(" + priceList[0] + ")";
            else
                price = priceList[0] + priceList[1];
            if (!price.isEmpty() && price.indexOf("нет в наличии") == -1)
                priceCurrent = priceCurrent + "\n" +
                        i.getKey() + ":" + price;
        }
        System.out.println(priceCurrent);
        return priceCurrent;
    }
}