package com.tomcat.parkir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.tomcat.parkir.DB.DB;
import com.tomcat.parkir.DB.DBHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ProgressDialog pDialog;
    private DB db;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);     //menghilangkan heading title
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();	//biar koneksi bisa dijalanin di main, karena aturannya koneksi gk boleh di Main langsung
        StrictMode.setThreadPolicy(policy);

        db = new DB(this);
        String auth[] = db.getAuth();              //apakah user dan password telah tersimpan

        if(!auth[1].equals("")){
            new Auth().execute();       //autentikasi user dan password ke server
        }
    }
    class Auth extends AsyncTask<Integer, Integer, Integer> {
        // ### Before starting background thread Show Progress Dialog ###
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        // ### Login Auth ###
        protected Integer doInBackground(Integer... args) {

            int signal=db.auth();
            if (signal==0){   //jika sukses
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
            return signal;
        }

        // ### After completing background task ###
        protected void onPostExecute(Integer signal) {
            // dismiss the dialog once done
            pDialog.dismiss();
            switch (signal) {
                case 0:
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), getString(R.string.mainFailedLogin), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal4), Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal2), Toast.LENGTH_SHORT).show();
                    //i = new Intent(getApplicationContext(), Login.class);
                    //startActivity(i);
                    //finish();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), getString(R.string.signal3), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void ClickDaftar(View V){
        //Intent i = new Intent(getApplicationContext(), DaftarActivity.class);
        //startActivity(i);
        //overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        //finish();
    }

    public void ClickLogin(View V){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }


}
