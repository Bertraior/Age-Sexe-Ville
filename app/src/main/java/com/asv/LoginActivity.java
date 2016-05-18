package com.asv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.asv.metier.Me;
import com.asv.ws.MeJson;


public class LoginActivity extends Activity implements View.OnClickListener {

    public final static String ID_CONNEXION = "idconnexion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signButton = (Button)findViewById(R.id.logButton);
        signButton.setOnClickListener(this);

        TextView alreadyText = (TextView)findViewById(R.id.forgetPasswordText);
        alreadyText.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        if (v.getId() == R.id.forgetPasswordText){
            String to = "contact@socialevent.fr";
            String subject = "mot de passe oublie";
            String message = "Mon adresse mail est : ";

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
            //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, message);

            //need this to prompts email client only
            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, "Choose an Email client :"));
            //secondeActivite = new Intent(MainActivity.this, ContactActivity.class);
        }

        if (v.getId() == R.id.logButton){
            connexion();
        }
    }

    public void connexion(){
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.chargement));
        progressDialog.setCancelable(true);
        progressDialog.setMax(300);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    //webservice
                    MeJson meJson = new MeJson();
                    EditText mailEditText = (EditText) findViewById(R.id.login);
                    EditText passwordEditText = (EditText) findViewById(R.id.password);

                    Me me = meJson.connexion(mailEditText.getText().toString(), passwordEditText.getText().toString(), null);
                    progressDialog.dismiss();

                    if(me != null){
                        SharedPreferences.Editor editor = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE).edit();
                        editor.putInt("idConnexion", me.getIduser());
                        editor.putString("mail", mailEditText.getText().toString());
                        editor.putString("pseudo", me.getPseudo());
                        editor.commit();
                        progressDialog.dismiss();
                        Intent secondeActivite = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(secondeActivite);
                        finish();
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                alertDialog.setTitle(getString(R.string.interneterror));
                                alertDialog.setMessage(getString(R.string.interneterrormessage));
                                alertDialog.show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                }catch(Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                            alertDialog.setTitle(getString(R.string.interneterror));
                            alertDialog.setMessage(getString(R.string.interneterrormessage));
                            alertDialog.show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
        thread.start();
    }
}
