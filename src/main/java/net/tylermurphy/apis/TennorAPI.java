package net.tylermurphy.apis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.tylermurphy.Config;

public class TennorAPI {
	
	public static String getUrlFromSearch(String search) {
		try {
			JSONObject json = getSearchResults(search, 50);
			JSONArray results = json.getJSONArray("results");
			int choice = (int) (Math.random()*results.length()-1);
			System.out.println(results.length());
			JSONObject resultsObject = (JSONObject) results.get(choice);
			JSONArray media = resultsObject.getJSONArray("media");
			JSONObject mediaObject = (JSONObject) media.get(0);
			JSONObject mediaType = mediaObject.getJSONObject("gif");
			return mediaType.getString("url");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

    private static JSONObject getSearchResults(String searchTerm, int limit) {

        final String url = String.format("https://api.tenor.com/v1/search?q=%1$s&key=%2$s&limit=%3$s",
                searchTerm, Config.TENNOR_API_KEY, limit);
        try {
            return get(url);
        } catch (IOException | JSONException ignored) {
        }
        return null;
    }

    private static JSONObject get(String url) throws IOException, JSONException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_CREATED) {
                String error = String.format("HTTP Code: '%1$s' from '%2$s'", statusCode, url);
                throw new ConnectException(error);
            }

            return parser(connection);
        } catch (Exception ignored) {
        	ignored.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new JSONObject("");
    }
    
    private static JSONObject parser(HttpURLConnection connection) throws JSONException {
        char[] buffer = new char[1024 * 4];
        int n;
        InputStream stream = null;
        try {
            stream = new BufferedInputStream(connection.getInputStream());
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            StringWriter writer = new StringWriter();
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
            return new JSONObject(writer.toString());
        } catch (IOException ignored) {
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return new JSONObject("");
    }
    
}

