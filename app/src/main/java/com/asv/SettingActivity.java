package com.asv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asv.ws.MeJson;
import com.asv.ws.MessagePriveJson;
import com.asv.metier.Message;
import com.google.android.gcm.GCMRegistrar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SettingActivity extends Activity implements View.OnClickListener {

    public final static String ID_CONNEXION = "idconnexion";
    ArrayList<Message> listeMessageslocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
        String pseudo = preferences.getString("pseudo", "pseudo");

        Thread thread2 = new Thread(new Runnable(){
            @Override
            public void run() {
            String regId = GCMRegistrar.getRegistrationId(SettingActivity.this);
            if(regId.equals("")){
                GCMRegistrar.register(SettingActivity.this, CommonUtilities.SENDER_ID);
            }else{
                SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
                int connexionId = preferences.getInt("idConnexion", 0);
                try{
                    //webservice
                    MeJson meJson = new MeJson();
                    meJson.majgcm(connexionId, regId);
                }catch(Exception e){

                }
            }
            }
        });
        thread2.start();

        EditText newPseudo = (EditText) findViewById(R.id.newPseudo);
        newPseudo.setText(pseudo);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
            //webservice
            MessagePriveJson messagePriveJson = new MessagePriveJson();

            SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
            int connexionId = preferences.getInt("idConnexion", 0);

            final ArrayList<Message> listeMessages = messagePriveJson.getAllMessage(connexionId);

            if(!listeMessages.isEmpty()){
                listeMessageslocal = listeMessages;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messagerie);
                        messageLayout.removeAllViews();

                        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formaterShow = new SimpleDateFormat("dd MMM");

                        int icolor = 0;

                        for (Message message : listeMessages) {
                            String dateToPrint;
                            try {
                                Date date = formater.parse(message.getDate().substring(0, 10));
                                dateToPrint = formaterShow.format(date);
                            } catch (Exception e) {
                                e.printStackTrace();
                                dateToPrint = message.getDate();
                            }

                            LinearLayout ll = new LinearLayout(getBaseContext());
                            ll.setPadding(0, 7, 0, 7);
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            ll.setLayoutParams(param);
                            ll.setOrientation(LinearLayout.VERTICAL);

                            ll.setId(message.getIdmessage());
                            if(icolor %2 == 0){
                                ll.setBackgroundColor(Color.parseColor("#EBE5E5"));
                            }else{
                                ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            }
                            icolor++;
                            ll.setOnClickListener(SettingActivity.this);

                            TextView pseudoText = new TextView(getBaseContext());
                            pseudoText.setText(message.getPseudo());
                            pseudoText.setPadding(12, 0, 12, 0);
                            pseudoText.setTextSize(12);
                            pseudoText.setTypeface(null, Typeface.BOLD);
                            pseudoText.setTextColor(Color.BLACK);

                            ll.addView(pseudoText);

                            TextView messageText = new TextView(getBaseContext());
                            messageText.setText(message.getMessage());
                            messageText.setPadding(12, 0, 12, 0);
                            messageText.setTextSize(12);
                            messageText.setTextColor(Color.BLACK);

                            ll.addView(messageText);

                            RelativeLayout relativeLayout = new RelativeLayout(getBaseContext());

                            TextView dateText = new TextView(getBaseContext());
                            dateText.setText(dateToPrint);
                            dateText.setTextSize(11);
                            dateText.setPadding(0, 0, 12, 0);
                            dateText.setGravity(Gravity.END);

                            ll.addView(dateText);

                            messageLayout.addView(ll);
                        }
                    }
                });
            }
            }
        });
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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
            modifyPseudo();
        }

        return super.onOptionsItemSelected(item);
    }

    public void modifyPseudo(){
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.chargement));
        progressDialog.setCancelable(true);
        progressDialog.setMax(200);
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //webservice
                MeJson meJson = new MeJson();

                SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
                int connexionId = preferences.getInt("idConnexion", 0);

                EditText newPseudo = (EditText) findViewById(R.id.newPseudo);

                Boolean resultat = meJson.modify_pseudo(connexionId, newPseudo.getText().toString());
                progressDialog.dismiss();
                if(resultat == true){
                    SharedPreferences.Editor editor = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE).edit();
                    editor.putString("pseudo", newPseudo.getText().toString());
                    editor.commit();
                    finish();
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
                            alertDialog.setTitle(getString(R.string.interneterror));
                            alertDialog.setMessage(getString(R.string.interneterrormessage));
                            alertDialog.show();

                        }
                    });
                }
            }
        });
        thread.start();
    }

    @Override
    public void onClick(View v) {
        doGoSelectmessage(v.getId());
    }

    public void doGoSelectmessage(int idmessage){
        Intent secondeActivite = new Intent(SettingActivity.this, AskMailActivity.class);

        SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
        int connexionId = preferences.getInt("idConnexion", 0);

        for (Message message : listeMessageslocal) {
            if(message.getIdmessage() == idmessage && message.getIduser() != connexionId){
                secondeActivite.putExtra("iduser", message.getIduser());
                secondeActivite.putExtra("pseudo", message.getPseudo());
                startActivity(secondeActivite);
            }
        }
    }
}
