package net.tylermurphy.apis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public abstract class API{

	protected static JSONObject getJson(String url) throws IOException, JSONException {
        HttpURLConnection connection = getConnection(url);
        try {
            return parseJson(connection);
        } catch (Exception ignored) {
        	ignored.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new JSONObject("");
    }
	
	protected static Document getXML(String url) throws IOException, JSONException {
        HttpURLConnection connection = getConnection(url);
        try {
            return parseXML(connection);
        } catch (Exception ignored) {
        	ignored.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
	
	private static HttpURLConnection getConnection(String url) {
		HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_CREATED) {
                String error = String.format("HTTP Code: '%1$s' from '%2$s'", statusCode, url);
                throw new ConnectException(error);
            }
            
            return connection;
        } catch (Exception e) { 
        	e.printStackTrace(); 
        }
        return null;
	}
	
	private static Document parseXML(HttpURLConnection connection) throws ParserConfigurationException, SAXException {
        InputStream stream = null;
        try {
            stream = new BufferedInputStream(connection.getInputStream());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (IOException ignored) {
        	ignored.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
	}
    
	private static JSONObject parseJson(HttpURLConnection connection) throws JSONException {
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