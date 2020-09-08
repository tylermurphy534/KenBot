package net.tylermurphy.apis;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class NekosLifeAPI extends API {
	
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
            return getJson(url);
        } catch (JSONException | IOException ignored) {}
        return null;
    }
    
}

