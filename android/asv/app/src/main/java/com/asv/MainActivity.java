package com.asv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.asv.adapter.ChatArrayAdapter;
import com.asv.metier.Chat;
import com.asv.ws.ChatJson;
import com.asv.ws.CheckUpdateJson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends Activity {

    public final static String ID_CONNEXION = "idconnexion";
    ListView listView;

    public static boolean createChatCallback = false;
    public static int idChatCallBack = 0;
    public static String nomChatCallBack = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkversion();
        /*
        SharedPreferences.Editor editor = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        */
        SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);

        int connexionId = preferences.getInt("idConnexion", 0);


        if (connexionId != 0) {
            String language = preferences.getString("language", "null");
            if(!language.equals("null")){
                Locale locale = new Locale(language);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config, null);
            }

            getActionBar();

            final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.chargement));
            progressDialog.setCancelable(true);
            progressDialog.setMax(200);
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    //webservice
                    ChatJson chatJson = new ChatJson();

                    SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
                    int connexionId = preferences.getInt("idConnexion", 0);

                    final ArrayList<Chat> listeChats = chatJson.getChat(connexionId);
                    if(!listeChats.isEmpty()){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int[] idChat = new int[listeChats.size()];
                                String[] nomChat = new String[listeChats.size()];
                                String[] pseudoChat = new String[listeChats.size()];
                                String[] dateChat = new String[listeChats.size()];
                                int[] nbParticipantChat = new int[listeChats.size()];

                                int i = 0;
                                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat formaterShow = new SimpleDateFormat("dd MMM");

                                for (Chat chat : listeChats) {
                                    idChat[i] = chat.getIdforumasv();
                                    nomChat[i] = chat.getNomforumasv();
                                    pseudoChat[i] = chat.getPseudoasv();

                                    try {
                                        Date date = formater.parse(chat.getDatecreationforumasv().substring(0, 10));
                                        dateChat[i] = formaterShow.format(date);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        dateChat[i] = chat.getDatecreationforumasv();
                                    }
                                    nbParticipantChat[i] = chat.getNbparticipant();
                                    i++;
                                }

                                listView = (ListView) findViewById(R.id.list);

                                listView.setAdapter(new ChatArrayAdapter(MainActivity.this, nomChat, pseudoChat, dateChat, nbParticipantChat));

                                final int[] idChatFinal = idChat;
                                final String[] nomChatFinal = nomChat;

                                listView.setOnItemClickListener(new OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {
                                        participerChat(idChatFinal[position], nomChatFinal[position]);
                                    }
                                });
                            }
                        });

                    }
                    progressDialog.dismiss();
                }
            });
            thread.start();
        }
        else{
            doGoPage(1);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(createChatCallback == true){
            createChatCallback = false;
            Intent secondeActivite = new Intent(MainActivity.this, EventActivity.class);
            secondeActivite.putExtra("idchat", idChatCallBack);
            secondeActivite.putExtra("nomChat", nomChatCallBack);
            startActivity(secondeActivite);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_settings:
                doGoPage(3);
                break;
            case R.id.refresh:
                refresh();
                break;
            case R.id.create_forum:
                doGoPage(2);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doGoPage(int choice) {
        Intent secondeActivite = null;
        switch(choice){
            case 1:
                secondeActivite = new Intent(MainActivity.this, SignActivity.class);
                break;
            case 2:
                secondeActivite = new Intent(MainActivity.this, CreateChatActivity.class);
                break;
            case 3:
                secondeActivite = new Intent(MainActivity.this, SettingActivity.class);
                break;
            case 11:
                secondeActivite = new Intent(MainActivity.this, MajVersionActivity.class);
                break;
        }
        startActivity(secondeActivite);
    }

    private void refresh(){
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.chargement));
        progressDialog.setCancelable(true);
        progressDialog.setMax(200);
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //webservice
                ChatJson chatJson = new ChatJson();

                SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
                int connexionId = preferences.getInt("idConnexion", 0);

                final ArrayList<Chat> listeChats = chatJson.getChat(connexionId);
                if(!listeChats.isEmpty()){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int[] idChat = new int[listeChats.size()];
                            String[] nomChat = new String[listeChats.size()];
                            String[] pseudoChat = new String[listeChats.size()];
                            String[] dateChat = new String[listeChats.size()];
                            int[] nbParticipantChat = new int[listeChats.size()];

                            int i = 0;
                            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat formaterShow = new SimpleDateFormat("dd MMM");

                            for (Chat chat : listeChats) {
                                idChat[i] = chat.getIdforumasv();
                                nomChat[i] = chat.getNomforumasv();
                                pseudoChat[i] = chat.getPseudoasv();

                                try {
                                    Date date = formater.parse(chat.getDatecreationforumasv().substring(0, 10));
                                    dateChat[i] = formaterShow.format(date);
                                }catch (Exception e){
                                    e.printStackTrace();
                                    dateChat[i] = chat.getDatecreationforumasv();
                                }
                                nbParticipantChat[i] = chat.getNbparticipant();
                                i++;
                            }

                            listView = (ListView) findViewById(R.id.list);

                            listView.invalidateViews();

                            listView.setAdapter(new ChatArrayAdapter(MainActivity.this, nomChat, pseudoChat, dateChat, nbParticipantChat));

                            final int[] idChatFinal = idChat;
                            final String[] nomChatFinal = nomChat;

                            listView.setOnItemClickListener(new OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    participerChat(idChatFinal[position], nomChatFinal[position]);
                                }
                            });
                        }
                    });

                }
                progressDialog.dismiss();
            }
        });
        thread.start();
    }

    public void participerChat(int idchat, String nomChat){
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.chargement));
        progressDialog.setCancelable(true);
        progressDialog.setMax(60);

        final int idChatFinal = idchat;
        final String nomChatFinal = nomChat;

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //webservice
                ChatJson chatJson = new ChatJson();

                SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
                int connexionId = preferences.getInt("idConnexion", 0);

                Boolean resultat = chatJson.participeChat(connexionId, idChatFinal);

                progressDialog.dismiss();
                if(resultat == false){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle(getString(R.string.forum_full));
                            alertDialog.setMessage(getString(R.string.forum_full_description));
                            alertDialog.show();
                        }
                    });
                }else{
                    Intent secondeActivite = new Intent(MainActivity.this, EventActivity.class);
                    secondeActivite.putExtra("idchat", idChatFinal);
                    secondeActivite.putExtra("nomChat", nomChatFinal);
                    startActivity(secondeActivite);
                }
            }
        });
        thread.start();
    }

    private void checkversion(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                int versionCode = 0;
                try {
                    versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                CheckUpdateJson checkUpdateJson = new CheckUpdateJson();

                final ArrayList<String> version = checkUpdateJson.getLastVersion();

                if(!version.isEmpty() && versionCode < Integer.parseInt(version.get(0))){
                    doGoPage(11);
                }
            }
        });
        thread.start();
    }
}
