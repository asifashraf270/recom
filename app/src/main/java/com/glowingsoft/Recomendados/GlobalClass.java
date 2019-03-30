package com.glowingsoft.Recomendados;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GlobalClass extends Application {
    Context context;
    public static GlobalClass singelton;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        singelton = this;
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPreference), MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public static GlobalClass getInstance() {
        return singelton;
    }

    public void SnackBar(View rootLayout, String text, int bgColor, int textColor) {
        Snackbar snackbar;
        snackbar = Snackbar.make(rootLayout, text, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        if (bgColor != -1) {
            view.setBackgroundColor(bgColor);
        }
        if (textColor != -1) {
            TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(textColor);
        }
        snackbar.show();
    }

    public boolean isNetworkAvailable() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }

    public void StoreUserId(String id) {
        editor.putString("id", id);
        editor.commit();
    }

    public String returnUserId() {
        return sharedPreferences.getString("id", null);
    }

    public void storeUserType(String type) {
        editor.putString("type", type);
        editor.commit();
    }

    public String returnUserType() {
        return sharedPreferences.getString("type", null);
    }


    /**
     *
     * 1=Login as a Buyer
     * 2=Login as a seller
     *
     */


}
