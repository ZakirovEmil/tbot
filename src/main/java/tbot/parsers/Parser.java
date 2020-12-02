package tbot.parsers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Parser {
    protected static Document connect(String link) throws IOException {
        return Jsoup.connect(link)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                .referrer("http://www.google.com")
                .get();
    }

    protected static Boolean linkIsCorrect(String link) {
        try {
            var resp = Jsoup.connect(link)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .ignoreHttpErrors(true)
                    .execute();
            System.out.println(resp.statusCode());
            return resp.statusCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
