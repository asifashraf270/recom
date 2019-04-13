package com.glowingsoft.Recomendados;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;

import java.util.regex.Pattern;

public class ParentClass extends AppCompatActivity {
    protected Intent intent;
    protected ProgressDialog progressDialog;
    protected Context context;
    protected RequestParams requestParams;

    protected void fullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    protected String getValueFromEdittext(EditText editText) {
        return editText.getText().toString();
    }

    protected String getUniqueId() {
        return Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    protected boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    protected void loadFragment(Fragment fragment, int container, String tag) {
        if (tag != null) {
            getSupportFragmentManager().beginTransaction().replace(container, fragment, tag).addToBackStack(tag).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(container, fragment).addToBackStack(null).commit();

        }
    }
}
