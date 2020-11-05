package tbot.parsers;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParserMetacritic {

    private Document document;
    private String link;
    private String nameGame;
    private String platformGame;
    private final String LINK_METACRITIC = "https://www.metacritic.com/game";

    public ParserMetacritic(String nameGame, String platformGame) throws IOException {
        this.nameGame = nameGame;
        this.platformGame = platformGame;
        link = creatLinkMetacritic();
        document = Jsoup.connect(link)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.google.com")
                .get();;
    }

    private String creatLinkMetacritic(){
        var link = LINK_METACRITIC +
                "/" + platformGame + "/" +
                nameGame.replace(" ", "-");
        System.out.println(link);
        return link;
    }

    public String getInfoMetacritic(){
        var str = "Critics score:" + getCriticsScore() +
                " " +
                "Users score:" + getUsersScore();
        return str;
    }

    private String getCriticsScore(){
        try {
            Elements text;
//                    .getElementsByClass("metascore_w xlarge game positive")
//                    .select("span")
//                    .text();
//            var text = document.getElementsByClass("metascore_w xlarge game positive") != null ?
//                    document.getElementsByClass("metascore_w xlarge game positive") :
//                    document.getElementsByClass("metascore_w xlarge game mixed") != null ?
//                            document.getElementsByClass("metascore_w xlarge game mixed") :
//                            document.getElementsByClass("metascore_w xlarge game negative") != null ?
//                            document.getElementsByClass("metascore_w xlarge game negative") : null;
            if (document.getElementsByClass("metascore_w xlarge game positive") != null){
                text = document.getElementsByClass("metascore_w xlarge game positive");
            } else if(document.getElementsByClass("metascore_w xlarge game mixed") != null){
                text = document.getElementsByClass("metascore_w xlarge game mixed");
            } else {
                text = document.getElementsByClass("metascore_w xlarge game negative");
            }
            System.out.println(text + "critics");
            return text.select("span").text();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "null";
    }

    private String getUsersScore(){
        try {
            Elements text;
//                    .getElementsByClass("metascore_w user large game positive")
//                    .first()
//                    .text();
//            var text = document.getElementsByClass("metascore_w user large game positive") != null ?
//                    document.getElementsByClass("metascore_w user large game positive") :
//                    document.getElementsByClass("metascore_w user large game mixed") != null ?
//                            document.getElementsByClass("metascore_w user large game mixed") :
//                            document.getElementsByClass("metascore_w user large game negative") != null ?
//                                    document.getElementsByClass("metascore_w user large game negative") : null;
            if (document.getElementsByClass("metascore_w user large game positive") != null){
                text = document.getElementsByClass("metascore_w user large game positive");
            } else if(document.getElementsByClass("metascore_w user large game mixed") != null){
                text = document.getElementsByClass("metascore_w user large game mixed");
            } else {
                text = document.getElementsByClass("metascore_w user large game negative");
            }
            System.out.println(text + "users");
            return text.first().text();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "null";
    }

}

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//
//public class InfoGame {
//    private String link ;
//
//    public InfoGame(String l) throws IOException {
//        link = l;
//        Document doc = Jsoup.connect(link).get();
//        Elements needs = doc.select("<div#class="metascore_w xlarge game positive">");
//        System.out.println("dads");
//    }
//}
