package net.tylermurphy.apis;

import java.io.IOException;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Rule34API extends API {

	public static String getUrlFromSearch(String search) {
		try {
			Document doc = getSearchResults(search);
			NodeList nList = doc.getElementsByTagName("post");
			int choice = (int) (Math.random()*nList.getLength()-1);
			Node node = nList.item(choice);
			Element element = (Element) node;
			return element.getAttribute("file_url");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	 private static Document getSearchResults(String searchTerm) { 	
        final String url = "https://rule34.xxx/index.php?page=dapi&s=post&q=index&tags="+ searchTerm;
        System.out.println(url);
        try {
            return getXML(url);
        } catch (JSONException | IOException ignored) {}
        return null;
	 }
	
}
