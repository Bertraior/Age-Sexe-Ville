package com.asv.ws;

import com.asv.metier.Chat;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bertraior on 24/06/2015.
 */
public class ChatJson {
    public ArrayList<Chat> getChat(int androidId) {
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        ArrayList<Chat> listeChats = new ArrayList<Chat>();

        try {
            String myurl= "http://geness.fr/asv/index.php/api/chat/chats/iduseridmobile/" + newId;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            JSONArray array = new JSONArray(InputStreamOperations.InputStreamToString(inputStream));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                Chat chat = new Chat();
                try{
                    chat.setIdforumasv(obj.getInt("idforumasv"));
                }catch(Exception e){

                }
                try{
                    chat.setPseudoasv(obj.getString("pseudoasv"));
                }catch(Exception e){

                }
                try{
                    chat.setNomforumasv(obj.getString("nomforumasv"));
                }catch(Exception e){

                }
                try{
                    chat.setDatecreationforumasv(obj.getString("datecreationforumasv"));
                }catch(Exception e){

                }
                try{
                    chat.setNbparticipant(obj.getInt("nbparticipant"));
                }catch(Exception e){

                }
                listeChats.add(chat);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // On retourne la liste des evenements
        return listeChats;
    }

    public int createChat(int androidId, String chatName) {
        int resultat = 0;
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        try {
            String myurl= "http://geness.fr/asv/index.php/api/chat/create/iduseridmobile/" + newId;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("chat", chatName));

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            DataFormatStringPost dataFormatStringPost = new DataFormatStringPost();
            wr.writeBytes(dataFormatStringPost.getQuery(params));
            wr.flush();
            wr.close();

            connection.connect();
            InputStream inputStream = connection.getInputStream();
            JSONArray array = new JSONArray(InputStreamOperations.InputStreamToString(inputStream));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                try{
                    resultat = obj.getInt("idforum");
                }catch(Exception e){

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultat;
    }

    public Boolean participeChat(int androidId, int idChat) {
        Boolean resultat = false;
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        try {
            String myurl= "http://geness.fr/asv/index.php/api/participant/participe/iduseridmobile/" + newId;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idforum", String.valueOf(idChat)));

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            DataFormatStringPost dataFormatStringPost = new DataFormatStringPost();
            wr.writeBytes(dataFormatStringPost.getQuery(params));
            wr.flush();
            wr.close();

            connection.connect();
            InputStream inputStream = connection.getInputStream();
            JSONArray array = new JSONArray(InputStreamOperations.InputStreamToString(inputStream));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                try{
                    resultat = obj.getBoolean("success");
                }catch(Exception e){

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultat;
    }

    public Boolean quitChat(int androidId, int idChat) {
        Boolean resultat = false;
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        try {
            String myurl= "http://geness.fr/asv/index.php/api/participant/quitte/iduseridmobile/" + newId;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idforum", String.valueOf(idChat)));

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            DataFormatStringPost dataFormatStringPost = new DataFormatStringPost();
            wr.writeBytes(dataFormatStringPost.getQuery(params));
            wr.flush();
            wr.close();

            connection.connect();
            InputStream inputStream = connection.getInputStream();
            JSONArray array = new JSONArray(InputStreamOperations.InputStreamToString(inputStream));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                try{
                    resultat = obj.getBoolean("success");
                }catch(Exception e){

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultat;
    }
}
