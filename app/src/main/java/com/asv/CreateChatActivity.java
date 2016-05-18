package com.asv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.asv.adapter.ChatArrayAdapter;
import com.asv.metier.Chat;
import com.asv.ws.ChatJson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CreateChatActivity extends Activity {

    public final static String ID_CONNEXION = "idconnexion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.validate) {
            validate();
        }

        return super.onOptionsItemSelected(item);
    }

    public void validate(){
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

                EditText chatTitle = (EditText) findViewById(R.id.nameChat);

                int resultat = chatJson.createChat(connexionId, chatTitle.getText().toString());

                progressDialog.dismiss();
                if(resultat == 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(CreateChatActivity.this).create();
                            alertDialog.setTitle(getString(R.string.interneterror));
                            alertDialog.setMessage(getString(R.string.interneterrormessage));
                            alertDialog.show();
                        }
                    });
                }else{
                    MainActivity.createChatCallback = true;
                    MainActivity.idChatCallBack = resultat;
                    MainActivity.nomChatCallBack = chatTitle.getText().toString();
                    finish();
                }
            }
        });
        thread.start();
    }
}
