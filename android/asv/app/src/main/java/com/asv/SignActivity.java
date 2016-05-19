package com.asv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.provider.Settings.Secure;

import com.asv.ws.MeJson;

import java.util.Locale;


public class SignActivity extends Activity implements View.OnClickListener {

    public final static String ID_CONNEXION = "idconnexion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        Button signButton = (Button)findViewById(R.id.signButton);
        signButton.setOnClickListener(this);

        /*
        TextView alreadyText = (TextView)findViewById(R.id.alreadyText);
        alreadyText.setOnClickListener(this);
        */
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE);

        int connexionId = preferences.getInt("idConnexion", 0);
        if (connexionId != 0) {
            Intent secondeActivite = new Intent(SignActivity.this, MainActivity.class);
            startActivity(secondeActivite);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign, menu);
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
        if (v.getId() == R.id.signButton){
            doGoPage(1);
        }
    }

    private void doGoPage(int choice) {
        Intent secondeActivite = null;
        switch(choice){
            case 1:
                inscription();
                break;
            case 2:
                secondeActivite = new Intent(SignActivity.this, LoginActivity.class);
                startActivity(secondeActivite);
                break;
        }
    }

    public void inscription(){
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.chargement));
        progressDialog.setCancelable(true);
        progressDialog.setMax(300);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    //webservice
                    MeJson meJson = new MeJson();
                    EditText pseudoEditText = (EditText) findViewById(R.id.pseudo);

                    int userId = meJson.inscription(Secure.getString(getContentResolver(),
                            Secure.ANDROID_ID), pseudoEditText.getText().toString(), "android", null);
                    progressDialog.dismiss();

                    if(userId != 0){
                        SharedPreferences.Editor editor = getSharedPreferences(ID_CONNEXION, MODE_PRIVATE).edit();
                        editor.putInt("idConnexion", userId);
                        editor.putString("mail", Secure.getString(getContentResolver(),
                                Secure.ANDROID_ID));
                        editor.putString("pseudo", pseudoEditText.getText().toString());
                        editor.commit();

                        progressDialog.dismiss();
                        Intent secondeActivite = new Intent(SignActivity.this, MainActivity.class);
                        startActivity(secondeActivite);
                        finish();
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(SignActivity.this).create();
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
                            AlertDialog alertDialog = new AlertDialog.Builder(SignActivity.this).create();
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
