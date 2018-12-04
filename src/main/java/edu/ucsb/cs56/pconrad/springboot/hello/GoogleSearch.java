package edu.ucsb.cs56.pconrad.springboot.hello;

import java.util.*;
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


public class GoogleSearch{
    
    static String apiKey = "AIzaSyBpVaXBM09Ui7J_ULzayl8F4sv5HnGYRKI";
    static String customSearchEngineKey = "27016364061886516:j7jvcqdxoxc";
    static String searchURL = "https://www.googleapis.com/customsearch/v1?";


    public static ArrayList<SearchResult> gSearch(SearchQuery query) {
  
	String file="GoogleApiKey.txt";
	/*try{
	    BufferedReader reader = new BufferedReader(new FileReader(file));
	    apiKey=reader.readLine();
	    customSearchEngineKey=reader.readLine();
	    reader.close();
	}
	catch (java.io.IOException e){
	    System.err.print("File is empty");
	}
	*/
	String toSearch = searchURL + "key=" + apiKey + "&cx=" + customSearchEngineKey+"&q=";

	String searchTerm=query.getUserEntry();
	try {
	    URL url=new URL(toSearch + URLEncoder.encode(searchTerm,"UTF-8"));
	    HttpsURLConnection connection=(HttpsURLConnection)url.openConnection();
	    connection.setRequestProperty("User-Agent","UCSB/1.0");
	    BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String line;
	    StringBuffer buffer=new StringBuffer();
	    while((line=input.readLine())!=null){
		buffer.append(line);
	    }
	    input.close();
	    System.out.println(buffer.toString());
	    ArrayList<SearchResult> results = new ArrayList<SearchResult>();
	    JsonParser parser = new JsonParser();
	    JsonObject json = parser.parse(input).getAsJsonObject();
	    json = parser.parse(json.get("webPages").toString()).getAsJsonObject();
	    JsonArray array = json.getAsJsonArray("value");
	    for(int i = 0; i < array.size(); ++i) {
		JsonObject jname = parser.parse(array.get(i).toString()).getAsJsonObject();
		String name = jname.get("title").toString();
		JsonObject jsnippet = parser.parse(array.get(i).toString()).getAsJsonObject();
		String snippet = jname.get("snippet").toString();
		JsonObject jurl_ = parser.parse(array.get(i).toString()).getAsJsonObject();
		String url_ = jname.get("link").toString();
		results.add(new SearchResult(name, snippet, url_));
	    }
	    return results;
	    
	}
	catch(Exception e){
	    e.printStackTrace(System.out);
	    System.exit(1);
	}
	return null;
    }
}