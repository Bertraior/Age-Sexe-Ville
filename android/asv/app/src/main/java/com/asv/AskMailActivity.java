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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asv.metier.Message;
import com.asv.ws.MessagePriveJson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AskMailActivity extends Activity implements View.OnClickListener {

    public final static String ID_CONNEXION = "idconnexion";
    private int idlastmessage = 0;

    ArrayList<Message> listeMessages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_mail);

        SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
        int connexionId = preferences.getInt("idConnexion", 0);

        Intent intent = getIntent();
        int iduser = intent.getExtras().getInt("iduser");
        String pseudo = intent.getExtras().getString("pseudo");

        ImageButton askButton = (ImageButton) findViewById(R.id.askMailButton);
        askButton.setOnClickListener(AskMailActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMessage();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.askMailButton){
            commente();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ask_mail, menu);
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
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void commente(){
        final ProgressDialog progressDialog = ProgressDialog.show(AskMailActivity.this, "", getString(R.string.chargement));
        progressDialog.setCancelable(true);
        progressDialog.setMax(200);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //webservice
                MessagePriveJson messageJson = new MessagePriveJson();

                SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
                int connexionId = preferences.getInt("idConnexion", 0);

                Intent intent = getIntent();
                int iduser = intent.getExtras().getInt("iduser");

                EditText jeCommentEdit = (EditText) findViewById(R.id.jeCommentEdit);

                Date aujourdhui = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                final boolean resultat = messageJson.sendMessage(connexionId, iduser, jeCommentEdit.getText().toString(), dateFormat.format(aujourdhui));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultat == true) {
                            EditText jeCommentEdit = (EditText) findViewById(R.id.jeCommentEdit);
                            jeCommentEdit.setText("");
                            loadMessage();
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(AskMailActivity.this).create();
                            alertDialog.setTitle(getString(R.string.forum_full));
                            alertDialog.setMessage(getString(R.string.forum_full_description));
                            alertDialog.show();
                        }

                    }
                });
                progressDialog.dismiss();
            }
        });
        thread.start();
    }

    public void loadMessage(){
        Intent intent = getIntent();
        final int iduser = intent.getExtras().getInt("iduser");

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.chargement));
        progressDialog.setCancelable(true);
        progressDialog.setMax(200);
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //webservice
                MessagePriveJson messageJson = new MessagePriveJson();

                SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
                int connexionId = preferences.getInt("idConnexion", 0);

                ArrayList<Message> checkMessagesEmpty = messageJson.getMessage(connexionId, iduser);
                if(checkMessagesEmpty.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messagesPrivee);
                        messageLayout.removeAllViews();

                        LinearLayout ll = new LinearLayout(getBaseContext());
                        ll.setPadding(0, 7, 0, 7);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        ll.setLayoutParams(param);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        ll.setBackgroundColor(Color.parseColor("#EBE5E5"));

                        TextView messageText = new TextView(getBaseContext());
                        messageText.setText(R.string.whatyourasv);
                        messageText.setPadding(12, 0, 12, 0);
                        messageText.setTextSize(12);
                        messageText.setTextColor(Color.BLACK);

                        ll.addView(messageText);
                        messageLayout.addView(ll);
                        }
                    });
                }else{
                    listeMessages = checkMessagesEmpty;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messagesPrivee);
                        messageLayout.removeAllViews();

                        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formaterShow = new SimpleDateFormat("dd MMM");

                        int icolor = 0;

                        for (Message message : listeMessages) {
                            idlastmessage = message.getIdmessage();

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
                            ll.setOnClickListener(AskMailActivity.this);

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
                progressDialog.dismiss();
            }
        });
        thread.start();
    }
}
