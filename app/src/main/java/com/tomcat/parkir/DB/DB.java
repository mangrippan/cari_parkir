package com.tomcat.parkir.DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tomcat.parkir.Parkir;
import com.tomcat.parkir.DB.DBCreate;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by albertbrucelee on 26/04/17.
 */

public class DB {

    DBHelper dbH; //DBHelper class buat ke server
    private Context context;
    public DB(Context context){
        this.context = context;

        String auth[] = getAuth();
        dbH = new DBHelper(this.context, auth[0], auth[1]);

        //new DBCreate(context);
    }
    public Parkir[] getListParkir(){

        int signal=dbH.getListParkir();       //ambil data dari server. Nanti data akan disimpan dulu di DBServer. signal buat melihat apakah query sukses atau failed
        if (signal==0){                     //jika sukses
            JSONArray json=dbH.get();        //ambil data di DBServer! Data akan seperti: [{"nama":"Albert"}]


            Parkir parkir[] = new Parkir[json.length()];

            try{
                for(int i=0; i<json.length(); i++){
                    JSONObject jData = json.getJSONObject(i);   //ambil data object ke 0
                    parkir[i] = new Parkir();
                    parkir[i].setId(jData.getInt("id"));
                    parkir[i].setLatitude(jData.getDouble("latitude"));
                    parkir[i].setLongitude(jData.getDouble("longitude"));
                    parkir[i].setName(jData.getString("name"));
                }
            }catch (JSONException e){                       //jika JSON error
                signal=1;
                Log.e("DB JParser", "Error parsing data " + e.toString());    //pesan ke logcat
            }
            return parkir;
        }
        else        //jika tidak sukses ambil data (error);
            return null;
    }
    public Parkir getDetailParkir(String parkirId){
        int signal=dbH.getDetailParkir(parkirId);       //ambil data dari server. Nanti data akan disimpan dulu di DBServer. signal buat melihat apakah query sukses atau failed
        if (signal==0){                     //jika sukses
            JSONArray json=dbH.get();        //ambil data di DBServer! Data akan seperti: [{"nama":"Albert"}]

            Parkir parkir = new Parkir();

            try{
                    JSONObject jData = json.getJSONObject(0);   //ambil data object ke 0
                    parkir.setId(jData.getInt("id"));
                    parkir.setLatitude(jData.getDouble("latitude"));
                    parkir.setLongitude(jData.getDouble("longitude"));
                    parkir.setName(jData.getString("name"));
                    parkir.setAddress(jData.getString("address"));
                    parkir.setPrice(jData.getString("price"));
                    parkir.setCapacity(jData.getInt("capacity"));
                    parkir.setAvailable(jData.getInt("available"));
            }catch (JSONException e){                       //jika JSON error
                signal=1;
                Log.e("DB JParser", "Error parsing data " + e.toString());    //pesan ke logcat
            }
            return parkir;
        }
        else        //jika tidak sukses ambil data (error);
            return null;
    }

    public boolean login(String id, String password){

        int signal=dbH.login(id,password);
        if (signal==0){

            JSONArray json=dbH.get();
            try{
                JSONObject jData = json.getJSONObject(0);
                setAuth(id, jData.getString("password"));   //simpan id dan password
                new DBCreate(context);
            }catch (JSONException e){
                Log.e("Login.class JSON Parser", "Error parsing data " + e.toString());
            }
            return true;
        }
        return false;
    }
    public int auth(){
        String auth[] = getAuth();
        return dbH.auth(auth[0],auth[1]);
    }
    public void setAuth(String id, String password){
        SharedPreferences sharedPref= context.getSharedPreferences("auth", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("id", id);
        editor.putString("password", password);
        editor.commit();
    }

    public String[] getAuth(){
        SharedPreferences sharedPref= context.getSharedPreferences("auth", 0);
        String auth[] = new String[2];
        auth[0] = sharedPref.getString("id", "");
        auth[1] = sharedPref.getString("password", "");
        Log.d("auth id",auth[0]);
        Log.d("auth id",auth[1]);
        return auth;
    }
    public String getId(){
        SharedPreferences sharedPref= context.getSharedPreferences("auth", 0);
        String id=sharedPref.getString("id", "");
        return id;
    }
    public void deleteAuth(){
        SharedPreferences sharedPref= context.getSharedPreferences("auth", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }
}
