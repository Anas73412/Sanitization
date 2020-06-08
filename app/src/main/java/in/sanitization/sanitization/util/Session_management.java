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
            , String mobile, String image, String wallet_ammount, String rewards, String house) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_HOUSE, house);
        editor.putString(KEY_WALLET_Ammount, wallet_ammount);
        editor.putString(KEY_REWARDS, rewards);

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
        // user email id
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, null));
        // user name
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, null));
        user.put(KEY_IMAGE, prefs.getString(KEY_IMAGE, null));
        user.put(KEY_WALLET_Ammount, prefs.getString(KEY_WALLET_Ammount, null));
        user.put(KEY_REWARDS, prefs.getString(KEY_REWARDS, null));
        user.put(KEY_HOUSE, prefs.getString(KEY_HOUSE, null));

        // return user
        return user;
    }

    public void updateData(String name, String mobile, String pincode
            , String socity_id, String image, String wallet, String rewards, String house) {

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_WALLET_Ammount, wallet);
        editor.putString(KEY_REWARDS_POINTS, rewards);
        editor.putString(KEY_HOUSE, house);

        editor.apply();
    }

//    public void updateSocity(String socity_name, String socity_id) {
//        editor.putString(KEY_SOCITY_NAME, socity_name);
//        editor.putString(KEY_SOCITY_ID, socity_id);
//
//        editor.apply();
//    }

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

//    public HashMap<String, String> getdatetime() {
//        HashMap<String, String> user = new HashMap<String, String>();
//
//        user.put(KEY_DATE, prefs2.getString(KEY_DATE, null));
//        user.put(KEY_TIME, prefs2.getString(KEY_TIME, null));
//
//        return user;
//    }

    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }
    public void updateProfile(String image,String name,String cnt)
    {
        editor.putString(KEY_IMAGE,image);
        editor.putString(KEY_NAME,name);
//        editor.putString(KEY_CNT,cnt);
        editor.commit();
    }

    public HashMap<String,String> getUpdateProfile()
    {
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_IMAGE,prefs.getString(KEY_IMAGE,null));
        map.put(KEY_NAME,prefs.getString(KEY_NAME,null));
//        map.put(KEY_CNT,prefs.getString(KEY_CNT,null));
        return map;
    }

    public void updateUserName(String name)
    {
        editor.putString(KEY_NAME,name);
        editor.commit();
    }
//    public void setCategoryId(String id)
//    {
//        editor.putString(KEY_CAT,id);
//        editor.commit();
//    }

//    public String getCategoryId()
//    {
//        return prefs.getString(KEY_CAT,"");
//    }

}
