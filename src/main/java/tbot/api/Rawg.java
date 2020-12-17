package tbot.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.javatuples.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Rawg {
    private static final String KEY = "key=886f0a4c93ec46e4941421be956ed1ad";
    private static final String USER_AGENT = "Mozilla/5.0 Firefox/26.0";

    public static Pair<String, String> searchGame(String game) throws IOException {
        return createAnswer(parserJson(connect(game)));
    }

    private static HttpResponse connect(String game) throws IOException {
        System.out.println(game);
        HttpClient httpclient = HttpClients.custom().setUserAgent(USER_AGENT).build();
        HttpGet httpget = new HttpGet(
                "https://api.rawg.io/api/games?" + KEY + "&search=" +
                        game.trim().replace(" ", "-").toLowerCase());

        System.out.println("Request Type: " + httpget.getMethod());
        return httpclient.execute(httpget);
    }

    private static Pair<String, String> createAnswer(List<String> strings) {
        return new Pair<>(
                "Название игра: " + strings.get(0) + "\n" +
                        "Релиз: " + strings.get(1) + "\n" +
                        "Metacritic: " + strings.get(2) + "\n" +
                        "Платформы: " + strings.get(3) + "\n",
                strings.get(4)
        );
    }

    private static List<String> parserJson(HttpResponse httpresponse) throws IOException {
        var json = EntityUtils.toString(httpresponse.getEntity(), "UTF-8");
        var strings = new ArrayList<String>();
        System.out.println(json);
        try {
            System.out.println("GO");
            JSONParser parser = new JSONParser();
            Object resultObject = parser.parse(json);
            if (resultObject instanceof JSONObject) {
                System.out.println(resultObject);
                JSONArray array = (JSONArray) ((JSONObject) resultObject).get("results");
                for (Object object : array) {
                    JSONObject obj =(JSONObject)object;
                    System.out.println("work1");
                    System.out.println(obj.getOrDefault("name", "Нету").toString());
                    System.out.println(obj.getOrDefault("released", "Нету").toString());
                    System.out.println(obj.getOrDefault("metacritic", "Нету").toString());
                    strings.add(obj.getOrDefault("name", "Нету").toString());
                    strings.add(obj.getOrDefault("released", "Нету").toString());
                    strings.add(obj.getOrDefault("metacritic", "Нету").toString());
                    StringBuilder platforms = new StringBuilder();
//                    for (Object platform : (JSONArray) obj.getOrDefault("platforms", "")) {
//                        var plarform_1 = ((JSONObject) platform).getOrDefault("platform", "");
//                        platforms.append(((JSONObject) plarform_1).getOrDefault("name", "").toString());
//                    }
                    strings.add("platforms");
                    strings.add(obj.getOrDefault("background_image", "Нету").toString());
                    break;
                }
                return strings;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
