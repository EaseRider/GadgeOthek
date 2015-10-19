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
        SERVER_ADRESS("http://152.96.238.75:8080/");

        private String def;
        E(String s) {
            def = s;
        }
        public String getDefault() {
            return def;
        }
    }

    public final String SERVER_ADRESS = "SERVER_ADRESS";


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
        //prefs.getStringSet(e.name(), );
    }




}
