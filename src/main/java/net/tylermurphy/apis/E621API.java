package net.tylermurphy.apis;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class E621API extends API {

	public static String getUrlFromSearch(String search) {
		try {
			JSONObject json = getSearchResults(search);
			System.out.println(json.toString());
			JSONArray results = json.getJSONArray("posts");
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
	        System.out.println(url);
	        try {
	            return getJson("GET",url,API.API_HEADERS_JSON_USERAGENT);
	        } catch (JSONException | IOException ignored) {}
	        return null;
	    }
	
}
