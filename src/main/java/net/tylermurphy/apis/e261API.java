package net.tylermurphy.apis;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class e261API extends API {

	public static String getUrlFromSearch(String search) {
		try {
			JSONObject json = getSearchResults(search);
			JSONArray results = (JSONArray) json.get("posts");
			int choice = (int) (Math.random()*results.length()-1);
			JSONObject post = (JSONObject) results.get(choice);
			JSONObject file = post.getJSONObject("file");
			String url = file.getString("url");
			return url;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	 private static JSONObject getSearchResults(String searchTerm) {
		 	
	        final String url = String.format("https://e621.net/posts.json?tags=%s", searchTerm);
	        try {
	            return get(url);
	        } catch (JSONException | IOException ignored) {}
	        return null;
	    }
	
}
