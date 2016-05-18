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
import com.asv.ws.ChatJson;
import com.asv.ws.MessageJson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class EventActivity extends Activity implements View.OnClickListener {

    public final static String ID_CONNEXION = "idconnexion";
    private boolean actif = true;
    private int idlastmessage = 0;

    ArrayList<Message> listeMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        String nomChat = intent.getExtras().getString("nomChat");
        TextView nomChatTextView = (TextView) findViewById(R.id.nameChat);

        nomChatTextView.setText(nomChat);
        getActionBar().setTitle(nomChat);

        ImageButton jeCommentButton = (ImageButton) findViewById(R.id.jeCommentButton);
        jeCommentButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        actif = true;
        Thread checkMessagethread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    while(actif){
                        checkMessage();
                        Thread.sleep(3000);
                    }
                }catch(Exception e){

                }
            }
        });
        checkMessagethread.start();

        loadMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        actif = false;

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //webservice
                ChatJson chatJson = new ChatJson();

                SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
                int connexionId = preferences.getInt("idConnexion", 0);

                Intent intent = getIntent();
                int idChat = intent.getExtras().getInt("idchat");
                chatJson.quitChat(connexionId, idChat);
            }
        });
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.jeCommentButton){
            commente();
        }else{
            doGoSelectmessage(v.getId());
        }
    }

    public void doGoSelectmessage(int idmessage){
        Intent secondeActivite = new Intent(EventActivity.this, AskMailActivity.class);

        SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
        int connexionId = preferences.getInt("idConnexion", 0);

        for (Message message : listeMessages) {
            if(message.getIdmessage() == idmessage && message.getIduser() != connexionId){
                actif = false;
                secondeActivite.putExtra("iduser", message.getIduser());
                secondeActivite.putExtra("pseudo", message.getPseudo());
                startActivity(secondeActivite);
            }
        }
    }

    public void commente(){
        final ProgressDialog progressDialog = ProgressDialog.show(EventActivity.this, "", getString(R.string.chargement));
        progressDialog.setCancelable(true);
        progressDialog.setMax(200);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //webservice
                MessageJson messageJson = new MessageJson();

                SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
                int connexionId = preferences.getInt("idConnexion", 0);

                Intent intent = getIntent();
                int idChat = intent.getExtras().getInt("idchat");

                EditText jeCommentEdit = (EditText) findViewById(R.id.jeCommentEdit);

                Date aujourdhui = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                final boolean resultat = messageJson.sendMessage(connexionId, idChat, jeCommentEdit.getText().toString(), dateFormat.format(aujourdhui));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    if (resultat == true) {
                        EditText jeCommentEdit = (EditText) findViewById(R.id.jeCommentEdit);
                        jeCommentEdit.setText("");
                        loadMessage();
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(EventActivity.this).create();
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
        final int idChat = intent.getExtras().getInt("idchat");

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.chargement));
        progressDialog.setCancelable(true);
        progressDialog.setMax(200);
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //webservice
                MessageJson messageJson = new MessageJson();

                SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
                int connexionId = preferences.getInt("idConnexion", 0);

                ArrayList<Message> checkMessagesEmpty = messageJson.getMessage(connexionId, idChat);
                if(checkMessagesEmpty.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messagesChat);
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
                            LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messagesChat);
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
                                ll.setOnClickListener(EventActivity.this);

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

    public void checkMessage(){
        SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);
        int connexionId = preferences.getInt("idConnexion", 0);

        Intent intent = getIntent();
        int idChat = intent.getExtras().getInt("idchat");

        MessageJson message = new MessageJson();
        ArrayList<Message> checkMessagesEmpty = message.checkMessage(connexionId, idChat, idlastmessage);
        if (checkMessagesEmpty.size() > 0){
            listeMessages = checkMessagesEmpty;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messagesChat);
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
                        if (icolor % 2 == 0) {
                            ll.setBackgroundColor(Color.parseColor("#EBE5E5"));
                        } else {
                            ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                        icolor++;
                        ll.setOnClickListener(EventActivity.this);

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

}
