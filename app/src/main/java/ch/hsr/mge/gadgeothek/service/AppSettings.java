package ch.hsr.mge.gadgeothek.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Galaxus on 19.10.2015.
 */
    public class AppSettings {
    private final SharedPreferences prefs;
    private final Context context;

    public enum E {
        // This is your local pc's IP, so change it!
        SERVER_ADRESS("http://152.96.236.69:8080"),
        //Customer ID of logged in User
        CUSTOMER_ID(""),
        // Security Token for session
        SECURITY_TOKEN("");

        private String def;
        E(String s) {
            def = s;
        }
        public String getDefault() {
            return def;
        }
    }

    public AppSettings(Context cont) {
        context = cont;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getSetting (E e) {
        return prefs.getString(e.name(), e.getDefault());
    }

    public void setSetting(E e, String val) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(e.name(), val);
        edit.commit();
    }




}
