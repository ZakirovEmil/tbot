package tbot.parsers;


import org.javatuples.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import tbot.service.TextMessages;

import java.io.IOException;

public class ParserMetacritic extends Parser {

    private static final String positiveCriticGameTag = "metascore_w xlarge game positive";
    private static final String positiveUserGameTag = "metascore_w user large game positive";
    private static final String mixedCriticGameTag = "metascore_w xlarge game mixed";
    private static final String mixedUserGameTag = "metascore_w user large game mixed";
    private static final String negativeCriticGameTag = "metascore_w xlarge game negative";
    private static final String negativeUserGameTag = "metascore_w user large game negative";
    private static final String officialNameGame = "product_title";
    private static final String developerGame = "summary_detail publisher";
    private static final String platformsGame = "summary_detail product_platforms";
    private static final String releaseGame = "summary_detail release_data";
    private static final String imageGameClass = "hg-block short-game-description";
    private static final String imageGameImgClass = "product_image large_image";

    public static String parseGame(String nameGame, String platformGame) throws IOException {
        var link = creatLinkMetacritic(platformGame, nameGame);
        if (!linkIsCorrect(link))
            return TextMessages.GAME_NOT_FOUND;
        var document = connect(link);
        var scoreRating = getScoreRating(document);
        var officialNameGame = getOfficialNameGame(document);
        var developerGame = getDeveloperGame(document);
        var releaseGame = getReleaseGame(document);
        var alsoPlatformsGame = getPlatformsGame(document);
        System.out.println(nameGame);
        System.out.println(platformGame);
        System.out.println(link);
        System.out.println(linkIsCorrect(link));
        return getResultGameStr(
                scoreRating.getValue0(),
                scoreRating.getValue1(),
                officialNameGame,
                developerGame,
                releaseGame,
                platformGame,
                alsoPlatformsGame
        );
    }

//    private static String getImageUrl(String nameGame) throws IOException {
//        var link = "https://hot-game.info/game/";
//        var link1 = "https://hot-game.info/game/" + nameGame.replace(" ", "-").toLowerCase();
//        if (linkIsCorrect(link1)){
//            link = link1;
//        }else {
//            var output = "";//все слова с заглавной буквы.
//            String[] words = nameGame.split("\" \"*");//разделяем на массив из слов
//            for(String word:words){
//                String first = word.substring(0,1).toUpperCase();
//                String all = word.substring(1);
//                output+=first+all;
//            }
//            link = link + output;
//        }
//        System.out.println(link);
//        var document = connect(link);
//        return document
//                .getElementsByClass(imageGameClass)
//                .select("img")
//                .attr("src");
//    }

    private static String creatLinkMetacritic(String platformGame, String nameGame) {
        String LINK_METACRITIC = "https://www.metacritic.com/game";
        var link = LINK_METACRITIC + "/" +
                platformGame.replace(" ", "-").toLowerCase() + "/" +
                nameGame.replace(" ", "-").toLowerCase();
        System.out.println(link);
        return link;
    }

    private static String getDeveloperGame(Document document) {
        return document
                .getElementsByClass(developerGame)
                .select(".data")
                .text()
                .trim();
    }

    private static String getReleaseGame(Document document) {
        return document
                .getElementsByClass(releaseGame)
                .select(".data")
                .text()
                .trim();
    }

    private static String getPlatformsGame(Document document) {
        return document
                .getElementsByClass(platformsGame)
                .select(".data").text().trim();
    }

    private static String getOfficialNameGame(Document document) {
        return document
                .getElementsByClass(officialNameGame)
                .select("h1")
                .text();
    }

    private static Pair<String, String> getScoreRating(Document document) {
        Elements userScore;
        Elements criticsScore;
        try {
            if (document.getElementsByClass(positiveUserGameTag).hasText()) {
                userScore = document.getElementsByClass(positiveUserGameTag);
            } else if (document.getElementsByClass(mixedUserGameTag).hasText()) {
                userScore = document.getElementsByClass(mixedUserGameTag);
            } else {
                userScore = document.getElementsByClass(negativeUserGameTag);
            }
            if (document.getElementsByClass(positiveCriticGameTag).hasText()) {
                criticsScore = document.getElementsByClass(positiveCriticGameTag);
            } else if (document.getElementsByClass(mixedCriticGameTag).hasText()) {
                criticsScore = document.getElementsByClass(mixedCriticGameTag);
            } else {
                criticsScore = document.getElementsByClass(negativeCriticGameTag);
            }
            System.out.println("Score work");
            return new Pair<String, String>(
                    criticsScore.text(),
                    userScore.first().text()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Pair<String, String>("Null", "Null");
    }

    private static String getResultGameStr(String criticScore, String userScore, String officNameGame,
                                           String developerGame, String releaseGame, String platformGame, String alsoPlatformsGame) {
        return "Навзвание игры: " + officNameGame + "\n" +
                "Разработчик: " + developerGame + "\n" +
                "Релиз: " + releaseGame + "\n" +
                "Платформы: " + alsoPlatformsGame + ", " + platformGame + "\n" +
                "Оценка критиков: " + criticScore + "\n" +
                "Оценка пользователей: " + userScore;
    }
}