package net.tylermurphy.apis;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

public abstract class API{
	
	public final static int API_HEADERS_NONE = 0;
	public final static int API_HEADERS_JSON = 1;
	public final static int API_HEADERS_JSON_USERAGENT = 2;
	public final static int API_HEADERS_USERAGENT = 3;

	protected static JSONObject getJson(String request, String url, int headerSetting, String... headers) throws IOException, JSONException {
        HttpURLConnection connection = getConnection(request, url, headerSetting, null, headers);
        try {
            return Parser.parseJson(connection.getInputStream());
        } catch (Exception ignored) {
        	ignored.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new JSONObject("");
    }
	
	protected static JSONObject getJson(String request, String url, int headerSetting, String body, String... headers) throws IOException, JSONException {
        HttpURLConnection connection = getConnection(request, url, headerSetting, body, headers);
        try {
            return Parser.parseJson(connection.getInputStream());
        } catch (Exception ignored) {
        	ignored.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new JSONObject("");
    }
	
	protected static Document getXML(String request, String url, int headerSetting, String... headers) throws IOException, JSONException {
        HttpURLConnection connection = getConnection(request, url, headerSetting, null, headers);
        try {
            return Parser.parseXML(connection.getInputStream());
        } catch (Exception ignored) {
        	ignored.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
	
	private static HttpURLConnection getConnection(String request, String url, int headerSetting, String body, String... headers) {
		HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(request);
            
            if(headerSetting == API_HEADERS_JSON) {
	            connection.setRequestProperty("Content-Type", "application/json");
	            connection.setRequestProperty("Accept", "application/json");
	            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            } else if(headerSetting == API_HEADERS_JSON_USERAGENT){
            	connection.setRequestProperty("Content-Type", "application/json");
	            connection.setRequestProperty("Accept", "application/json");
	            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            } else if(headerSetting == API_HEADERS_USERAGENT){
	            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            }
            
            for(int i=0; i+1<headers.length; i+=2) {
            	connection.addRequestProperty(headers[i], headers[i+1]);
            }
            
            if(body != null) {
	            try(OutputStream os = connection.getOutputStream()) {
	                byte[] input = body.getBytes("utf-8");
	                os.write(input, 0, input.length);			
	            }
            }
            
//            int statusCode = connection.getResponseCode();
//            if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_CREATED) {
//                String error = String.format("HTTP Code: '%1$s' from '%2$s'", statusCode, url);
//                throw new ConnectException(error);
//            }            
            
            return connection;
        } catch (Exception e) { 
        	e.printStackTrace(); 
        }
        return null;
	}
	
}
