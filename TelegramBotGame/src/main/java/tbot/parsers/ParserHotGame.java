//package tbot.parsers;
//
//import org.javatuples.Pair;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//public class ParserHotGame {
//
//    private static final String steamString = "title=\"Steam\"";
//    private static final String originString = "title=\"Origin\"";
//    private static final String gogString = "title=\"gog.com\"";
//    private static final String uplayString = "title=\"Ubisoft Store\"";
//    private static final String egsString = "title=\"www.epicgames.com\"";
//    private static final String errorTag = "module errorcode_module error404_module";
//
//    public static String parseGame(String nameGame, String platformGame) throws IOException {
//        var link = creatLinkHotGames(platformGame, nameGame);
//        var document = Connect(link);
//        if (!linkIsCorrect(document))
//            return "Мы не нашли вашу игру на Metacritic :(";
//        var scoreRating = getPrice(document);
//        System.out.println(nameGame);
//        System.out.println(platformGame);
//        System.out.println(link);
//        System.out.println(linkIsCorrect(document));
//        return getResultGameStr(
//                scoreRating.getValue0(),
//                scoreRating.getValue1()
//        );
//    }
//
//    private static Document Connect(String link) throws IOException {
//        return Jsoup.connect(link)
//                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
//                .referrer("http://www.google.com")
//                .ignoreHttpErrors(true)
//                .get();
//    }
//
//    private static HashMap<String, String> getPrice(Document document) {
//        String RawString;
//        HashMap<String, String> PriceList = new HashMap<>() {
//            String SteamPirce;
//            String OriginPrice;
//            String UplayPrice;
//            String GogPrice;
//            String EGSPrice;
//        };
//// if (document.getElementsByClass(positiveUserGameTag).hasText()) {
//// System.out.println("work1");
//// userScore = document.getElementsByClass(positiveUserGameTag);
//// criticsScore = document.getElementsByClass(positiveCriticGameTag);
//// } else if (document.getElementsByClass(mixedUserGameTag).hasText()) {
//// System.out.println("work2");
//// userScore = document.getElementsByClass(mixedUserGameTag);
//// criticsScore = document.getElementsByClass(mixedCriticGameTag);
//// } else {
//// System.out.println("work3");
//// userScore = document.getElementsByClass(negativeUserGameTag);
//// criticsScore = document.getElementsByClass(negativeCriticGameTag);
//// }
//// System.out.println("Score work");
//// return new Pair<String, String>(
//// criticsScore.text(),
//// userScore.first().text()
//// );
//        try {
//            RawString = document.getElementsByClass("game-prices").attr();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        return PriceList;
//    }
//
//    private static String magSearch(String gamePriceList, String Store) {
//        var indexStore = gamePriceList.indexOf(Store);
//        var changedPriceList = gamePriceList.substring(indexStore);
//
//    }
//}
