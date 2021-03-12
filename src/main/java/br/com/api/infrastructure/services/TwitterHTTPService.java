package br.com.api.infrastructure.services;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Service
public class TwitterHTTPService {
    private Gson gson;
    private String result;
    private HttpClient httpClient;

    private final String MARIA_TWITTER =
            "AAAAAAAAAAAAAAAAAAAAAFDqLAEAAAAAv7EIDP6SL7HDi04YtpKUNSg2cgA%3Dg7lfTmxD5grbygdM8bYcS91NoAu1ZE9yjN8laiMsTGLcLoLDFi";
    private final String EXPERIMENTAL_APP_TWITTER =
            "AAAAAAAAAAAAAAAAAAAAANI9LwEAAAAAqmlt%2BV0qebJK2uDSxpzFcwbn4vA%3DpIQSkka6DVnOzOKZYxVZsI2elu8RuBu6yIvHGrRczU5Ltsx28P";
    private final String EXPERIMENTAL_APP2_TWITTER =
            "AAAAAAAAAAAAAAAAAAAAAAwWMAEAAAAAPWN5W0XM5KoGClDsSByKJKQ2diw%3DS4jsnoKN8h8jTWJ1UZ9qUYeY0Jpu1LRh2T6jmX3sN4qoRS7XyE";

    public TwitterHTTPService getInstance() {
        this.gson = new GsonBuilder().serializeNulls().create();

        this.httpClient =
                HttpClients.custom()
                        .setDefaultRequestConfig(
                                RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                        .build();

        return this;
    }

    public TwitterHTTPService getTweetsFromTimelineUserId(Long userId)
            throws IOException, URISyntaxException {
        try {
            URIBuilder uriBuilder = new URIBuilder(String
                    .format("https://api.twitter.com/2/users/%s/tweets", Long.toString(userId)));

            ArrayList<NameValuePair> queryParameters;
            queryParameters = new ArrayList<>();
            queryParameters.addAll(getFieldsParameters());
            queryParameters.add(new BasicNameValuePair("max_results", "50"));
            uriBuilder.addParameters(queryParameters);

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setHeader("Authorization", String.format("Bearer %s", getAccessTokenDefault()));
            httpGet.setHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                this.result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException ex) {
            throw new IOException("Couldn't possible to connect to the server");

        } catch (URISyntaxException ex) {
            throw new URISyntaxException("https://api.twitter.com/2/users/s/tweets",
                    "Couldn't possible to parse the URI");
        }

        return this;
    }

    public TwitterHTTPService getASingleTweetById(Long id) throws IOException, URISyntaxException {
        try {
            URIBuilder uriBuilder = new URIBuilder(
                    String.format("https://api.twitter.com/2/tweets/%s", Long.toString(id)));

            ArrayList<NameValuePair> queryParameters;
            queryParameters = new ArrayList<>();
            queryParameters.addAll(getFieldsParameters());
            uriBuilder.addParameters(queryParameters);

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setHeader("Authorization", String.format("Bearer %s", getAccessTokenDefault()));
            httpGet.setHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                this.result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException ex) {
            throw new IOException("Couldn't possible to connect to the server");

        } catch (URISyntaxException ex) {
            throw new URISyntaxException("https://api.twitter.com/2/tweets/",
                    "Couldn't possible to parse the URI");
        }

        return this;
    }

    private String getAccessTokenDefault() {
        // return MARIA_TWITTER;
        return EXPERIMENTAL_APP_TWITTER;
        // return EXPERIMENTAL_APP2_TWITTER;
    }

    public JsonObject getResult() {
        Type typeToken = new TypeToken<JsonObject>() {
        }.getType();

        if (!this.result.contains("Rate limit exceeded")) {
            return this.gson.fromJson(this.result, typeToken);
        } else {
            JsonObject error = new JsonObject();
            error.addProperty("error", this.result);

            return error;
        }
    }

    private List<NameValuePair> getFieldsParameters() {
        List<NameValuePair> parameters = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("author_id,");
        sb.append("created_at,");
        sb.append("entities,");
        sb.append("source,");
        sb.append("public_metrics,");
        sb.append("in_reply_to_user_id,");
        sb.append("referenced_tweets,");
        sb.append("context_annotations,");
        sb.append("lang");

        parameters.add(new BasicNameValuePair("tweet.fields", sb.toString()));

        return parameters;
    }
}
