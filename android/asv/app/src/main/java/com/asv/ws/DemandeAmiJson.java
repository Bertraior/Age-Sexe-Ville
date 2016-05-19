package com.asv.ws;

import com.asv.metier.Chat;
import com.asv.metier.DemandeAmi;
import com.asv.metier.Me;
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
import java.util.List;

/**
 * Created by Bertraior on 24/06/2015.
 */
public class DemandeAmiJson {

    public ArrayList<DemandeAmi> checkDemandeAmi(int androidId, int iduser) {
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        ArrayList<DemandeAmi> listeResultat = new ArrayList<DemandeAmi>();
        try {
            String myurl= "http://geness.fr/asv/index.php/api/demanderami/checkdemande/iduseridmobile/" + newId + "/iduser/" + iduser;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            String result = "{\"resultat\":"+InputStreamOperations.InputStreamToString(inputStream)+"}";
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = new JSONArray(jsonObject.getString("resultat"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                DemandeAmi demanderAmi = new DemandeAmi();
                try{
                    demanderAmi.setStatut(obj.getInt("statutasvdemander"));
                }catch(Exception e){

                }
                try{
                    demanderAmi.setMail(obj.getString("mailasv"));
                }catch(Exception e){

                }
                listeResultat.add(demanderAmi);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // On retourne la liste des evenements
        return listeResultat;
    }

    public Boolean demanderAmi(int androidId, int iduser) {
        Boolean resultat = false;
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        try {
            String myurl= "http://geness.fr/asv/index.php/api/demanderami/demander/iduseridmobile/" + newId;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("iduser", String.valueOf(iduser)));

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            DataFormatStringPost dataFormatStringPost = new DataFormatStringPost();
            wr.writeBytes(dataFormatStringPost.getQuery(params));
            wr.flush();
            wr.close();

            connection.connect();
            InputStream inputStream = connection.getInputStream();
            String result = "{\"result\":"+InputStreamOperations.InputStreamToString(inputStream)+"}";
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = new JSONArray(jsonObject.getString("result"));
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

    public ArrayList<DemandeAmi> getDemande(int androidId) {
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        ArrayList<DemandeAmi> listeDemande = new ArrayList<DemandeAmi>();

        try {
            String myurl= "http://geness.fr/asv/index.php/api/demanderami/loaddemande/iduseridmobile/" + newId;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            String result = "{\"demandeami\":"+InputStreamOperations.InputStreamToString(inputStream)+"}";
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = new JSONArray(jsonObject.getString("demandeami"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                DemandeAmi demandeAmi = new DemandeAmi();
                try{
                    demandeAmi.setIduser(obj.getInt("iduserasv"));
                }catch(Exception e){

                }
                try{
                    demandeAmi.setPseudo(obj.getString("pseudoasv"));
                }catch(Exception e){

                }
                try{
                    demandeAmi.setMail(obj.getString("pseudoasv"));
                }catch(Exception e){

                }
                try{
                    demandeAmi.setStatut(obj.getInt("statutasvdemander"));
                }catch(Exception e){

                }
                listeDemande.add(demandeAmi);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // On retourne la liste des evenements
        return listeDemande;
    }

    public Boolean accepterAmi(int androidId, int iduser) {
        Boolean resultat = false;
        Cryptage crypteur = new Cryptage();
        String newId = crypteur.crypte(androidId);
        try {
            String myurl= "http://geness.fr/asv/index.php/api/demanderami/accepter/iduseridmobile/" + newId;

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("iduser", String.valueOf(iduser)));

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            DataFormatStringPost dataFormatStringPost = new DataFormatStringPost();
            wr.writeBytes(dataFormatStringPost.getQuery(params));
            wr.flush();
            wr.close();

            connection.connect();
            InputStream inputStream = connection.getInputStream();
            String result = "{\"result\":"+InputStreamOperations.InputStreamToString(inputStream)+"}";
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = new JSONArray(jsonObject.getString("result"));
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
