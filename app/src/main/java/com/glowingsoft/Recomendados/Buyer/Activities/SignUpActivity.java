package com.glowingsoft.Recomendados.Buyer.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.ParentClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends ParentClass implements View.OnClickListener {
    EditText userEt, passwordEt, emailEt;
    TextView signupTv;
    String userName, userEmail, userPassword;
    ImageView backIv;
    RelativeLayout rootLayout;
    TextView loginTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_sign_up);
        context = SignUpActivity.this;
        viewBinding();
    }

    private void viewBinding() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        backIv = findViewById(R.id.backIv);
        userEt = findViewById(R.id.userEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        signupTv = findViewById(R.id.signUpBtnTv);
        signupTv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        loginTv = findViewById(R.id.signUpTv);
        loginTv.setOnClickListener(this);
        rootLayout = findViewById(R.id.rootLayout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtnTv:
                signUpBtn(userEt, emailEt, passwordEt);
                break;
            case R.id.backIv:
                finish();
                break;
            case R.id.signUpTv:
                finish();
                break;
        }

    }

    private void signUpBtn(EditText userEt, EditText emailEt, EditText passwordEt) {
        userEmail = getValueFromEdittext(emailEt);
        userName = getValueFromEdittext(userEt);
        userPassword = getValueFromEdittext(passwordEt);
        if (userEmail.length() > 0 && userName.length() > 0 && userPassword.length() > 0 && isValidEmailId(userEmail)) {
            if (GlobalClass.getInstance().isNetworkAvailable()) {
                requestParams = new RequestParams();
                requestParams.put("name", userName);
                requestParams.put("email", userEmail);
                requestParams.put("password", userPassword);
                requestParams.put("device_id", getUniqueId());
                requestParams.put("device_type", "android");
                requestParams.put("is_social", "0");
                WebReq.post(Urls.signup, requestParams, new SignUpApi());

            } else {
                GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
            }
        } else {
            if (userName.length() > 0) {
                if (userEmail.length() > 0) {
                    if (isValidEmailId(userEmail)) {
                        if (userPassword.length() > 0) {

                        } else {
                            passwordEt.setError("Password is required");
                        }

                    } else {
                        emailEt.setError("Email is Invalid");
                    }
                } else {
                    emailEt.setError("Email is required");
                }


            } else {
                userEt.setError("User Name is required");
            }
        }
    }

    /*
    Sign up Rest Api
     */
    public class SignUpApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            progressDialog.show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getInt("status") == 200) {
                    JSONObject jsonObject = response.getJSONObject("user");
                    GlobalClass.getInstance().StoreUserId(jsonObject.getString("id"));
                    String title = response.getString("title");
                    GlobalClass.getInstance().storeTitle(title);
                    GlobalClass.getInstance().SnackBar(rootLayout, response.getString("message"), -1, -1);
                    intent = new Intent(SignUpActivity.this, BottomNavigationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    GlobalClass.getInstance().storeUserType("2");
                    startActivity(intent);
                    finish();
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, response.getString("message"), -1, -1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            GlobalClass.getInstance().SnackBar(rootLayout, throwable.getMessage(), -1, -1);
            progressDialog.dismiss();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }
    }
}
