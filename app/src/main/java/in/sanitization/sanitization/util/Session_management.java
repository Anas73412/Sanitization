package in.sanitization.sanitization.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import in.sanitization.sanitization.LoginActivity;

import static in.sanitization.sanitization.Config.Constants.*;

public class Session_management {

    SharedPreferences prefs;
    SharedPreferences prefs2;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {

        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();

        prefs2 = context.getSharedPreferences(PREFS_NAME2, PRIVATE_MODE);
        editor2 = prefs2.edit();

    }

    public void createLoginSession(String id, String email, String name
            , String mobile, String state, String city, String pin, String house,String dis_id,String area_id) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_STATE, state);
        editor.putString(KEY_ADDRESS, house);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_PINCODE, pin);
        editor.putString(KEY_SOCITY_ID, "");
        editor.putString(KEY_SOCITY_NAME, "");
        editor.putString(KEY_SOCITY_PINCODE, "");
        editor.putString(KEY_DISTRICT_MANAGER, dis_id);
        editor.putString(KEY_AREA_MANAGER, area_id);

        editor.commit();
    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {
            Intent loginsucces = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);
        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, prefs.getString(KEY_ID, null));
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, null));
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, null));
        user.put(KEY_CITY, prefs.getString(KEY_CITY, null));
        user.put(KEY_STATE, prefs.getString(KEY_STATE, null));
        user.put(KEY_PINCODE, prefs.getString(KEY_PINCODE, null));
        user.put(KEY_ADDRESS, prefs.getString(KEY_ADDRESS, null));
        user.put(KEY_DISTRICT_MANAGER, prefs.getString(KEY_DISTRICT_MANAGER, null));
        user.put(KEY_AREA_MANAGER, prefs.getString(KEY_AREA_MANAGER, null));
        return user;
    }

    public HashMap<String,String> getSocityDetails()
    {
        HashMap<String, String> socity = new HashMap<String, String>();
        socity.put(KEY_SOCITY_ID, prefs.getString(KEY_SOCITY_ID, null));
        socity.put(KEY_SOCITY_NAME, prefs.getString(KEY_SOCITY_NAME, null));
        socity.put(KEY_SOCITY_PINCODE, prefs.getString(KEY_SOCITY_PINCODE, null));
        return socity;

    }
    public void updateData(String name, String mobile, String pincode
            , String city, String state, String house) {

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_STATE, state);

        editor.putString(KEY_ADDRESS, house);

        editor.apply();
    }

    public void updateSocity(String socity_name, String socity_id,String socity_pincode) {
        editor.putString(KEY_SOCITY_NAME, socity_name);
        editor.putString(KEY_SOCITY_ID, socity_id);
        editor.putString(KEY_SOCITY_PINCODE, socity_pincode);
        editor.apply();
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void logoutSessionwithchangepassword() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

//    public void creatdatetime(String date, String time) {
//        editor2.putString(KEY_DATE, date);
//        editor2.putString(KEY_TIME, time);
//
//        editor2.commit();
//    }

    public void cleardatetime() {
        editor2.clear();
        editor2.commit();
    }



    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

    public void updateProfile(String name , String email ,String state ,String city ,String pin ,String add )
    {

        editor.putString(KEY_NAME,name);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_STATE,state);
        editor.putString(KEY_CITY,city);
        editor.putString(KEY_PINCODE,pin);
        editor.putString(KEY_ADDRESS,add);

        editor.commit();
    }

    public HashMap<String,String> getUpdateProfile()
    {
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_NAME,prefs.getString(KEY_NAME,null));
//        map.put(KEY_CNT,prefs.getString(KEY_CNT,null));
        return map;
    }

    public void clearSocities()
    {
        editor.putString(KEY_SOCITY_ID,"");
        editor.putString(KEY_SOCITY_NAME,"");
        editor.putString(KEY_SOCITY_PINCODE,"");
        editor.commit();
    }

}
