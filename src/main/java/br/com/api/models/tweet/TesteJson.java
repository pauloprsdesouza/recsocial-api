package br.com.api.models.tweet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import br.com.api.models.twitteruser.TwitterUserDataJson;


public class TesteJson {
    public static void main(String[] args) {
        try {
            // create Gson instance
            Gson gson = new Gson();

            try (FileReader reader = new FileReader(
                    "D:\\twitter-api-app\\src\\main\\java\\br\\com\\api\\models\\tweet\\tweet.json")) {
                JsonObject object = gson.fromJson(reader, JsonObject.class);

                JsonArray tweetsJson = object.get("data").getAsJsonArray();

                Type typeToken = new TypeToken<List<TweetDataJson>>() {
                }.getType();

                List<TweetDataJson> tweetsDataJson = gson.fromJson(tweetsJson, typeToken);

                for (JsonElement elementTweet : tweetsJson) {
                    JsonObject tweetJson = elementTweet.getAsJsonObject();
                    TweetDataJson teste = gson.fromJson(tweetJson, TweetDataJson.class);

                    FileReader readerUser = new FileReader(
                            "D:\\twitter-api-app\\src\\main\\java\\br\\com\\api\\models\\tweet\\twitteruser.json");

                    JsonObject userJson = gson.fromJson(readerUser, JsonObject.class);

                    JsonObject userData = userJson.get("data").getAsJsonObject();

                    TwitterUserDataJson twitterUser =
                            gson.fromJson(userData, TwitterUserDataJson.class);

                    System.out.println();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // create a reader



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
