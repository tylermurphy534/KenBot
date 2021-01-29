package net.tylermurphy.apis.nhentai;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.tylermurphy.apis.API;

public class NHentaiAPI extends API {
	
	private static final HashMap<Integer, Comic> cache = new HashMap<Integer, Comic>();

	public static Comic getComicFromSearch(String search) {
		try {
			JSONObject json = getSearchResults(search);
			System.out.println(json.toString());
			JSONArray results = json.getJSONArray("result");
			int choice = (int) (Math.random()*results.length()-1);
			JSONObject post = (JSONObject) results.get(choice);
			int id = post.getInt("id");
			if(cache.containsKey(id)) {
				return cache.get(id);
			}
			JSONObject info = getPostInformation(id);
			Comic c = new Comic();
			c.pages = info.getJSONObject("images").getJSONArray("pages").length();
			c.page_file_type = new String[c.pages];
			Iterator<Object> itr = info.getJSONObject("images").getJSONArray("pages").iterator();
			int i = 0;
			while(itr.hasNext()) {
				JSONObject o = (JSONObject) itr.next();
				c.page_file_type[i] = o.getString("t").equals("j") ? "jpg" : "png";
				i++;
			}
			c.cover_file_type = info.getJSONObject("images").getJSONObject("cover").getString("t").equals("j") ? "jpg" : "png";;
			c.media_id = info.getString("media_id");
			c.title = info.getJSONObject("title").getString("english");
			c.main_id = id;
			return c;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static JSONObject getSearchResults(String searchTerm) {
        final String url = String.format("https://nhentai.net/api/galleries/search?query=%s%s", searchTerm, "%20language:english");
        System.out.println(url);
        try {
            return getJson(url);
        } catch (JSONException | IOException ignored) {}
        return null;
     }
	
	private static JSONObject getPostInformation(int id) {
        final String url = String.format("https://nhentai.net/api/gallery/%s", id);
        System.out.println(url);
        try {
            return getJson(url);
        } catch (JSONException | IOException ignored) {}
        return null;
     }
	
}