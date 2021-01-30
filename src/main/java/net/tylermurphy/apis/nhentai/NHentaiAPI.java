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
			int id;
			JSONObject info;
			if(search.length() == 6 && isNumber(search)) {
				id = Integer.parseInt(search);
				info = getPostInformation(id);
			} else {
				JSONObject json = getSearchResults(search);
				System.out.println(json.toString());
				JSONArray results = json.getJSONArray("result");
				int choice = (int) (Math.random()*results.length()-1);
				JSONObject post = (JSONObject) results.get(choice);
				id = post.getInt("id");
				info = getPostInformation(id);
				if(info == null) {
					return null;
				}
			}
			if(cache.containsKey(id)) {
				return cache.get(id);
			}
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
		} catch (Exception e) {
			return null;
		}
	}
	
	private static boolean isNumber(String test) {
		try {
			Integer.parseInt(test);
			return true;
		} catch (Exception e){
			return false;
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