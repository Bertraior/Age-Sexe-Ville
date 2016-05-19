package com.asv.ws;

import com.asv.metier.Me;

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
public class MeJson {
    public Me connexion(String adresseMail, String password, String regId){
        Me me = new Me();
        try {
            String myurl= "http://geness.fr/asv/index.php/api/user/connect";

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", adresseMail));
            params.add(new BasicNameValuePair("password", password));
            //params.add(new BasicNameValuePair("redid", regId));

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
                    me.setIduser(obj.getInt("iduserasv"));
                    me.setPseudo(obj.getString("pseudoasv"));
                }catch(Exception e){

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return me;
    }

    public int inscription(String adresseMail, String pseudo, String password, String regId){
        int resultat = 0;
        Cryptage crypteur = new Cryptage();
        String cocoId = crypteur.crypte(0);

        try {
            String myurl= "http://geness.fr/asv/index.php/api/user/inscription";

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", adresseMail));
            params.add(new BasicNameValuePair("pseudo", pseudo));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("coco", cocoId));
            params.add(new BasicNameValuePair("redid", "0"));

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
                    resultat = obj.getInt("iduser");
                }catch(Exception e){

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultat;
    }

    public boolean changePassword(String adresseMail, String oldPassword ,String newPassword, int androidId){
        boolean resultat = false;
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        try {
            String myurl= "http://geness.fr/asv/index.php/api/user/changepassword/iduseridmobile/" + newId;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mail", adresseMail));
            params.add(new BasicNameValuePair("oldpassword", oldPassword));
            params.add(new BasicNameValuePair("newpassword", newPassword));

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

    public boolean modify_pseudo(int androidId, String pseudo){
        boolean resultat = false;
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        try {
            String myurl= "http://geness.fr/asv/index.php/api/user/modify_pseudo/iduseridmobile/" + newId;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pseudo", pseudo));

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
