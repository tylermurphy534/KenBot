package net.tylermurphy.apis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class NekosLifeAPI {
	
	public static String getUrlFromSearch(String search) {
		try {
			JSONObject json = getSearchResults(search);
			return json.getString("url");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

    private static JSONObject getSearchResults(String searchTerm) {

        final String url = String.format("https://nekos.life/api/v2/img/%s", searchTerm);
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
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            connection.connect();
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

