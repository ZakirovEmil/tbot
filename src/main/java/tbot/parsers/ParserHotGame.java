package tbot.parsers;


import org.javatuples.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import tbot.service.TextButtons;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import tbot.service.TextMessages;

import java.io.IOException;
import java.util.*;


public class ParserHotGame extends Parser {

    private static final String imageGameClass = "hg-block short-game-description";
    private static final String imageGameImgClass = "product_image large_image";
    private static final String rubles = "₽";

    private static final HashMap<String, String> websiteKey = new HashMap<>();

    public static Pair<String, String> parseGame(String nameGame, String namePlatform) throws IOException {
        fillWebsiteKey();
        System.out.println(namePlatform);
        var link = creatLinkHotGames(nameGame);
        if (!linkIsCorrect(link))
            return new Pair<>(
                    TextMessages.PRICE_NOT_FOUND,
                    TextMessages.PRICE_NOT_FOUND
            );
        var document = connect(link);
        return new Pair<>(getPrice(document, namePlatform), getImageUrl(document));
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

    private static String getImageUrl(Document document) throws IOException {
        return document
                .getElementsByClass(imageGameClass)
                .select("img")
                .attr("src");
    }


    private static String creatLinkHotGames(String nameGame) {
        var link = "https://hot-game.info/game/";
        var nameGameForLink = nameGame.replace(" ", "-").toLowerCase();
        var link1 = link + nameGameForLink;
        System.out.println(link1 + "1111111111");
        if (linkIsCorrect(link1)) {
            link = link1;
        } else {
            var correctNameGameForLink = "";
            String[] words = nameGameForLink.split("-");
            for (String word : words) {
                String firstLetter = word.substring(0, 1).toUpperCase();
                String otherLetters = word.substring(1);
                correctNameGameForLink += firstLetter + otherLetters + " ";
            }
            System.out.println(correctNameGameForLink);
            link = link + correctNameGameForLink.trim().replace(" ", "-");
        }
        System.out.println(link);
        return link;
    }

    private static String getPrice(Document document, String namePlatform) {
        Elements html = null;
        try {
            html = document.getElementsByClass("game-prices");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return magSearch(html);
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
                        priceList[1] + " (" + priceList[0] + ")";
            else
                price = priceList[0];
            if (!price.isEmpty() && price.indexOf("нет в наличии") == -1)
                priceCurrent = priceCurrent + "\n" +
                        i.getKey() + ": " + price;
        }
        System.out.println(priceCurrent);
        return priceCurrent;
    }
}