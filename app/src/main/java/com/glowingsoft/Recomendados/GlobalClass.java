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

    public void storeTitle(String title) {
        editor.putString("title", title);
        editor.commit();
    }

    public String returntitle() {
        return sharedPreferences.getString("title", null);
    }

    /**
     * 1=Login as a Buyer
     * 2=Login as a seller
     */
    public void logoOut() {
        editor.clear().commit();
    }

    public void storePreferenceScreenData(String latitude, String longitude, String currencyId, String languageId, String shoptype, String country, String city, String address) {
        editor.putString("lat", latitude);
        editor.putString("lon", longitude);
        editor.putString("currencyId", currencyId);
        editor.putString("langId", languageId);
        editor.putString("shopType", shoptype);
        editor.putString("city", city);
        editor.putString("country", country);
        editor.putString("address", address);
        editor.commit();
    }

    public String returnShoptype() {
        return sharedPreferences.getString("shopType", null);
    }

    public void storeShopName(String name) {
        editor.putString("shopName", name);
        editor.commit();
    }

    public String returnLatitude() {
        return sharedPreferences.getString("lat", null);
    }

    public String returnLongitude() {
        return sharedPreferences.getString("lon", null);
    }

    public String returncurrencyId() {
        return sharedPreferences.getString("currencyId", null);

    }

    public String returnLangId() {
        return sharedPreferences.getString("langId", null);
    }

    public String returncityName() {
        return sharedPreferences.getString("city", null);
    }

    public String returnCountryName() {
        return sharedPreferences.getString("country", null);
    }

    public String returnAddress() {
        return sharedPreferences.getString("address", null);
    }

    public String returnshopName() {
        return sharedPreferences.getString("shopName", null);
    }

    public void storeShopId(String id) {
        editor.putString("shopId", id);
        editor.commit();
    }

    public String returnShopId() {
        return sharedPreferences.getString("shopId", null);
    }

    public void storeFcmToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public String returnFcmToken() {
        return sharedPreferences.getString("token", null);
    }

    public void storeBadgeValue(int value) {
        editor.putInt("count", value);
        editor.commit();
    }

    public int returnCount() {
        return sharedPreferences.getInt("count", -1);
    }

    public void storeConvertionalId(String id) {
        editor.putString("conversition_id", id);
        editor.commit();
    }

    public String returnConvertionalId() {
        return sharedPreferences.getString("conversition_id", null);
    }

    public void removeConId() {
        editor.remove("conversition_id");
        editor.commit();
        editor.apply();
    }

}
