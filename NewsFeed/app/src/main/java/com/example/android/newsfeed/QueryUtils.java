package com.example.android.newsfeed;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SahilKapoor on 23-09-2017.
 */

public class QueryUtils {

    private static final String LOG_TAGS = QueryUtils.class.getSimpleName();

    /**
     * Default constructor
     */
    public QueryUtils() {
    }

    public static List<NewsFeed> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAGS, "Problem making HTTP request", e);
        }
        List<NewsFeed> news = extractFeaturesFromJson(jsonResponse);
        return news;
    }

    /**
     * Returns new URL object from the given string URL
     */
    private static URL createUrl(String strUrl) {

        URL url = null;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAGS, "Problem building Url", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as a response
     */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAGS, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAGS, "Problem retrieving news from json response", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Converting the InputStrem into a String which contains the whole json response
     */
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder result = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                result.append(line);
                line = reader.readLine();
            }
        }
        return result.toString();
    }

    /**
     * Return a list of news
     */
    private static List<NewsFeed> extractFeaturesFromJson(String NewsJson) {

        if (TextUtils.isEmpty(NewsJson)) {
            return null;
        }

        List<NewsFeed> newsFeeds = new ArrayList<>();
        try {

            JSONObject baseJsonResponse = new JSONObject(NewsJson);

            if (baseJsonResponse.has("response")) {
                JSONObject responseObj = baseJsonResponse.getJSONObject("response");
                JSONArray resultsArray = responseObj.getJSONArray("results");


                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject currentNews = resultsArray.getJSONObject(i);

                    String newsTitle = currentNews.getString("sectionName");
                    String newsDescription = currentNews.getString("webTitle");
                    String newsType = currentNews.getString("type");
                    String newsDate = currentNews.getString("webPublicationDate");
                    String newsUrl = currentNews.getString("webUrl");

                    newsFeeds.add(new NewsFeed(newsTitle, newsDescription, newsType, newsDate, newsUrl));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAGS, "Problem solving the news json results", e);
        }
        return newsFeeds;
    }
}
