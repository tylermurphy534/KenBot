package net.tylermurphy.Kenbot.apis;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

public abstract class API{
	
	public static int SUCCESS = 0;
	public static int FALIURE = 1;
	public static int DUPLICATE = 2;
	public static int NOT_FOUND = 4;
	
	protected static String getResponse(String requestMeathod, String url, String... headers) throws IOException, JSONException {
		return getResponse(requestMeathod, url, null, headers);
	}
	
	protected static String getResponse(String requestMeathod, String url, String body, String... headers) throws IOException, JSONException {
        
		HttpURLConnection connection = getConnection(requestMeathod, url, body, headers);
        try {
            return Parser.parseText(connection.getInputStream());
        } catch (Exception ignored) {
    		return connection.getResponseCode()+"";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

	protected static JSONObject getJson(String requestMeathod, String url, String... headers) throws IOException, JSONException {
        return getJson(requestMeathod, url, null, headers);
    }
	
	protected static JSONObject getJson(String requestMeathod, String url, String body, String... headers) throws IOException, JSONException {
        
		String[] all_headers = new String[headers.length+2];
        for(int i=0; i<headers.length; i++)
        	all_headers[i] = headers[i];
        all_headers[all_headers.length-2] = "Content-Type";
        all_headers[all_headers.length-1] = "application/json";
        
		HttpURLConnection connection = getConnection(requestMeathod, url, body, all_headers);
        try {
            return Parser.parseJson(connection.getInputStream());
        } catch (Exception ignored) {
        	System.out.println(connection.getResponseCode() + " " + connection.getResponseMessage());
    		JSONObject j = new JSONObject("{\"status\":\""+connection.getResponseCode()+"\"}");
    		return j;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
	
	protected static Document getXML(String requestMeathod, String url, String... headers) throws IOException, JSONException {
        return getXML(requestMeathod, url, null, headers);
    }
	
	protected static Document getXML(String requestMeathod, String url, String body, String... headers) throws IOException {
       
		String[] all_headers = new String[headers.length+2];
        for(int i=0; i<headers.length; i++)
        	all_headers[i] = headers[i];
        all_headers[all_headers.length-2] = "Content-Type";
        all_headers[all_headers.length-1] = "text/xml";
		
		HttpURLConnection connection = getConnection(requestMeathod, url, body, all_headers);
        try {
            return Parser.parseXML(connection.getInputStream());
        } catch (Exception ignored) {
        	return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
	
	private static HttpURLConnection getConnection(String requestMeathod, String url, String body, String... headers) {
		HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(requestMeathod);
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            
            for(int i=0; i+1<headers.length; i+=2) {
            	connection.addRequestProperty(headers[i], headers[i+1]);
            }
            
            if(body != null) {
	            try(OutputStream os = connection.getOutputStream()) {
	                byte[] input = body.getBytes("utf-8");
	                os.write(input, 0, input.length);		
	            } catch (Exception e) {
	            	throw new Exception("Error writing body to HTTP connection.\n"+e.getMessage());
	            }
            }           
            
            return connection;
        } catch (Exception e) { 
        	e.printStackTrace(); 
        }
        return null;
	}
	
}
