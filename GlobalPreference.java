package com.nextgen.emailbot;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalPreference {

    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;

    private Context context;
    public GlobalPreference(Context context) {
        sharedPreference = context.getSharedPreferences("sample",MODE_PRIVATE);
        editor = sharedPreference.edit();
    }
    public void saveIP(String ipaddress){
        editor.putString("ip",ipaddress);
        editor.apply();
    }
    public String getIP(){
        return  sharedPreference.getString("ip","");
    }

    public void saveID(String uid){
        editor.putString("user_id",uid);
        editor.apply();
    }
    public String getID(){
        return  sharedPreference.getString("user_id","");
    }

    public void saveName(String name){
        editor.putString("name",name);
        editor.apply();
    }
    public String getName(){
        return  sharedPreference.getString("name","");
    }

    public void saveEmail(String email){
        editor.putString("email",email);
        editor.apply();
    }
    public String getEmail(){
        return  sharedPreference.getString("email","");
    }

    public void saveLoginStatus(Boolean loginStatus){
        editor.putBoolean("loginStatus",loginStatus);
        editor.apply();
    }
    public Boolean getLoginStatus(){
        return  sharedPreference.getBoolean("loginStatus",false);
    }
}
