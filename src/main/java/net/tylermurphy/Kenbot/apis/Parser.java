package net.tylermurphy.Kenbot.apis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Parser {

	public static Document parseXML(InputStream is) throws ParserConfigurationException, SAXException {
		BufferedInputStream stream = null;
        try {
            stream = new BufferedInputStream(is);
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
                } catch (IOException ignored) {}
            }
        }
        return null;
	}
    
	@SuppressWarnings("resource")
	public static JSONObject parseJson(InputStream is) throws JSONException {
		InputStreamReader reader = null;
		BufferedInputStream stream = null;
        char[] buffer = new char[1024 * 4];
        int n;
        try {
            stream = new BufferedInputStream(is);
            reader = new InputStreamReader(stream, "UTF-8");
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
                } catch (IOException ignored) {}
            }
        	if (reader != null) {
                try {
                	reader.close();
                } catch (IOException ignored) {}
            }
        }
        return new JSONObject("");
    }
	
	public static String parseText(InputStream is) throws JSONException {
		InputStreamReader reader = null;
		BufferedInputStream stream = null;
        char[] buffer = new char[1024 * 4];
        int n;
        try {
            stream = new BufferedInputStream(stream);
            reader = new InputStreamReader(stream, "UTF-8");
            StringWriter writer = new StringWriter();
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
            return writer.toString();
        } catch (IOException ignored) {
        } finally {
        	if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignored) {}
            }
        	if (reader != null) {
                try {
                	reader.close();
                } catch (IOException ignored) {}
            }
        }
        return null;
    }
	
	
	
}
