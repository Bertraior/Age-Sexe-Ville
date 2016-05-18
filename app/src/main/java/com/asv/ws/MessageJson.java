package com.asv.ws;

import com.asv.metier.Chat;
import com.asv.metier.Message;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bertraior on 26/06/2015.
 */
public class MessageJson {
    public ArrayList<Message> getMessage(int androidId, int idChat) {
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        ArrayList<Message> listeMessage = new ArrayList<Message>();
        try {
            String myurl= "http://geness.fr/asv/index.php/api/commentforum/commentsforum/iduseridmobile/" + newId + "/idforum/" + idChat;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            JSONArray array = new JSONArray(InputStreamOperations.InputStreamToString(inputStream));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                Message message = new Message();
                try{
                    message.setIdmessage(obj.getInt("idasvcommenter"));
                }catch(Exception e){

                }
                try{
                    message.setIduser(obj.getInt("idusercommenterasv"));
                }catch(Exception e){

                }
                try{
                    message.setIdforum(obj.getInt("idforumcommenterasv"));
                }catch(Exception e){

                }

                try{
                    message.setPseudo(obj.getString("pseudocommenterasv"));
                }catch(Exception e){

                }
                try{
                    message.setMessage(obj.getString("messagecommenterasv"));
                }catch(Exception e){

                }
                try{
                    message.setDate(obj.getString("datecommenterasv"));
                }catch(Exception e){

                }
                listeMessage.add(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // On retourne la liste des evenements
        return listeMessage;
    }

    public ArrayList<Message> getMessageById(int androidId, String idMessage) {
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        ArrayList<Message> listeMessage = new ArrayList<Message>();
        try {
            String myurl= "http://geness.fr/asv/index.php/api/commentforum/thecommentforum/iduseridmobile/" + newId + "/idmessage/" + idMessage;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            JSONArray array = new JSONArray(InputStreamOperations.InputStreamToString(inputStream));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                Message message = new Message();
                try{
                    message.setIdmessage(obj.getInt("idasvcommenter"));
                }catch(Exception e){

                }
                try{
                    message.setIduser(obj.getInt("idusercommenterasv"));
                }catch(Exception e){

                }
                try{
                    message.setIdforum(obj.getInt("idforumcommenterasv"));
                }catch(Exception e){

                }

                try{
                    message.setPseudo(obj.getString("pseudocommenterasv"));
                }catch(Exception e){

                }
                try{
                    message.setMessage(obj.getString("messagecommenterasv"));
                }catch(Exception e){

                }
                try{
                    message.setDate(obj.getString("datecommenterasv"));
                }catch(Exception e){

                }
                listeMessage.add(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // On retourne la liste des evenements
        return listeMessage;
    }

    public Boolean sendMessage(int androidId, int idforum, String message, String date) {
        boolean resultat = false;
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        try {
            String myurl= "http://geness.fr/asv/index.php/api/commentforum/commente/iduseridmobile/" + newId + "/idforum/" + idforum;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("message", message));
            params.add(new BasicNameValuePair("datemobile", date));

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

    public ArrayList<Message> checkMessage(int androidId, int idChat, int idmessage) {
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        ArrayList<Message> listeMessage = new ArrayList<Message>();
        try {
            String myurl= "http://geness.fr/asv/index.php/api/commentforum/check/iduseridmobile/" + newId + "/idforum/" + idChat + "/idmessage/" + idmessage;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            JSONArray array = new JSONArray(InputStreamOperations.InputStreamToString(inputStream));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                Message message = new Message();
                try{
                    message.setIdmessage(obj.getInt("idasvcommenter"));
                }catch(Exception e){

                }
                try{
                    message.setIduser(obj.getInt("idusercommenterasv"));
                }catch(Exception e){

                }
                try{
                    message.setIdforum(obj.getInt("idforumcommenterasv"));
                }catch(Exception e){

                }

                try{
                    message.setPseudo(obj.getString("pseudocommenterasv"));
                }catch(Exception e){

                }
                try{
                    message.setMessage(obj.getString("messagecommenterasv"));
                }catch(Exception e){

                }
                try{
                    message.setDate(obj.getString("datecommenterasv"));
                }catch(Exception e){

                }
                listeMessage.add(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // On retourne la liste des evenements
        return listeMessage;
    }
}
