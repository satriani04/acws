package com.fisipol.winarto.sistempelaporanstudi.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.fisipol.winarto.sistempelaporanstudi.activity.LoginActivity;
import com.fisipol.winarto.sistempelaporanstudi.activity.MainActivity;

import java.util.HashMap;

/**
 * Created by winarto on 6/27/2016.
 */
public class SessionManager {
    //sharedpreferences reference
    SharedPreferences pref;

    //editor untuk sharedpreferences
    SharedPreferences.Editor editor;

    //context
    Context _context;

    //sharedpref mode
    int PRIVATE_MODE = 0;

    //sharedpreferences name
    private static final String PREFER_NAME = "SipsPref";

    //all shared preference keya
    private static final String IS_USER_LOGIN = "IsUserLogedIn";

    // User name (make variable public to access from outside)
    //nama user (buat variable public untuk akses dari luar)
    public static final String KEY_NAME = "name";

    //niu mahasiswa
    public static final String KEY_NIU = "niu";

    //untuk intro slider
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";


    //construktor
    public SessionManager(Context ctx) {
        this._context = ctx;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //create login session
    //buat login session
    public void createUserLoginSession(String name, String niu) {
        //simpan login value = true
        editor.putBoolean(IS_USER_LOGIN, true);

        //simpan nama user di pref
        editor.putString(KEY_NAME, name);


        editor.putString(KEY_NIU, niu);

        //commit semua perubahan
        editor.commit();
    }

    //check for login
    public boolean isUserLogedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */

    /*
    * cek login method akan mengecek status login user. jika false maka akan redirect ke login page
    * */
    public boolean checkLogin() {
        //cek login status
        if (!this.isUserLogedIn()) {
            // user is not logged in redirect him to Login Activity
            // user yang belum login akan redirect ke activity login
            Intent i = new Intent(_context, LoginActivity.class);

            //closing all activity from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //add new flag to start activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //start activity
            _context.startActivity(i);
            return true;

        }

        return false;
    }

    /*
    * get stored session data
    * */
    public HashMap<String, String> getUserDetail() {
        //gunakan hashmap untuk menyimpan user detail
        HashMap<String, String> user = new HashMap<String, String>();

        //nama
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));


        //idUser
        user.put(KEY_NIU, pref.getString(KEY_NIU, null));

        return user;
    }

    //hapus session
    public void logout() {
        //clear semua user data dari sharedpreferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        //setelah logout redirect ke login
        Intent i = new Intent(_context, LoginActivity.class);

        //close all activity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //add new flag to start activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }


    //fungsi untu mengckeck intro slider
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
