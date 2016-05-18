package com.asv.ws;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CheckUpdateJson {
	public ArrayList<String> getLastVersion() {
		ArrayList<String> listeVersion = new ArrayList<String>();
        try {
            String myurl= "http://geness.fr/asv/index.php/api/myversion/versionandroid";

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
           
            String result = "{\"version\":"+InputStreamOperations.InputStreamToString(inputStream)+"}";
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = new JSONArray(InputStreamOperations.InputStreamToString(inputStream));
            for (int i = 0; i < array.length(); i++) {
            	JSONObject obj = new JSONObject(array.getString(i));
            	String version = null;
            	try{
            		version = obj.getString("version");
            	}catch(Exception e){
            		
            	}
            	listeVersion.add(version);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listeVersion;
    }
}
