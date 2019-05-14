package android.lan.new_info1;

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

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //Empty constructor, must be private
    private QueryUtils() {
            }

    public static List<NewsInfo> fetchNewsData(String requestUrl) {


        //Create URL object
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

       List<NewsInfo> newsInfo = extractFeatureFromJson(jsonResponse);

        return newsInfo;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }


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
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the newinfo JSON result.", e);
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

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
            line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<NewsInfo> extractFeatureFromJson(String newsinfoJSON) {
        if (TextUtils.isEmpty(newsinfoJSON)) {
            return null;
        }

        List<NewsInfo> newsInfo = new ArrayList<>();
        try {

            JSONObject root = new JSONObject(newsinfoJSON);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");


            for (int i = 0; i <results.length(); i++) {

                JSONObject item = results.getJSONObject(i);
                String id = item.getString("sectionId");
                String webTitle = item.getString("webTitle");
                String url = item.getString("webUrl");
                String dateTime = item.getString("webPublicationDate");

                //Extract the value for the key webtitle, under the tags array
                String author;

                //traverse to the next array in tags
                JSONArray tags = item.getJSONArray("tags");
                if (tags != null && tags.length() > 0) {
                    JSONObject tagsObject = tags.getJSONObject(0);
                    author = tagsObject.optString("webTitle", "No Author");
                } else author = "No Author";

                NewsInfo newitems = new NewsInfo(id, webTitle, url, dateTime, author);
                newsInfo.add(newitems);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problems parsing the Guardian news JSON results", e);
        }
        return newsInfo;
    }


}
