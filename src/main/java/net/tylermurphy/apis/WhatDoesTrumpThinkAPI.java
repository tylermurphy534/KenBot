package net.tylermurphy.apis;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class WhatDoesTrumpThinkAPI extends API{
	
	public static String getQuote() {
		try {
			JSONObject json = get("https://api.whatdoestrumpthink.com/api/v1/quotes/random");
			return json.getString("message");
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}

