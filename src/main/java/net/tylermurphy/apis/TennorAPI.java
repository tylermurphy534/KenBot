package net.tylermurphy.apis;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.tylermurphy.Config;

public class TennorAPI extends API {
	
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
            return getJson(url);
        } catch (JSONException | IOException ignored) {
        }
        return null;
    }
    
}

