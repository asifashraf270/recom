package com.glowingsoft.Recomendados.Buyer.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.ParentClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.ShopPreferencesActivity;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class LoginInActivity extends ParentClass implements View.OnClickListener {
    EditText emailEt, passwordEt;
    TextView forgotTv, loginTv, signupTv;
    String email, password;
    RelativeLayout rootLayout;
    CallbackManager callbackManager;
    LoginButton loginButton;
    ImageView fbTv;
    String social_type;
    GoogleSignInClient googleSignInClient;
    ImageView googleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_login_in);
        context = LoginInActivity.this;
        viewBinding();

    }

    private void viewBinding() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        callbackManager = CallbackManager.Factory.create();
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        googleTv = findViewById(R.id.googleTv);
        if (GlobalClass.getInstance().isNetworkAvailable()) {
            googleTv.setOnClickListener(this);
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
        }
        forgotTv = findViewById(R.id.forgotTv);
        forgotTv.setOnClickListener(this);
        loginTv = findViewById(R.id.loginTv);
        signupTv = findViewById(R.id.signUpTv);
        signupTv.setOnClickListener(this);
        rootLayout = findViewById(R.id.rootLayout);
        loginTv.setOnClickListener(this);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        fbTv = findViewById(R.id.fbTv);
        if (GlobalClass.getInstance().isNetworkAvailable()) {
            fbTv.setOnClickListener(this);
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
        }
        loginButton = (LoginButton) findViewById(R.id.login_button);

        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog.dismiss();
                AccessToken accessToken = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String name = object.getString("name"); // 01/31/1980 format
                                    String id = object.getString("id");
                                    String email = object.getString("email");
                                    if (GlobalClass.getInstance().isNetworkAvailable()) {
                                        requestParams = new RequestParams();
                                        requestParams.put("name", name);
                                        requestParams.put("email", email);
                                        requestParams.put("password", "123456323");
                                        requestParams.put("device_id", getUniqueId());
                                        requestParams.put("device_type", "android");
                                        requestParams.put("is_social", "1");
                                        requestParams.put("social_id", id);
                                        requestParams.put("social_type", social_type);
                                        WebReq.post(Urls.signup, requestParams, new LoginInApi());
                                    } else {
                                        GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                progressDialog.dismiss();
                GlobalClass.getInstance().SnackBar(rootLayout, "Request Cancel", -1, -1);
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                progressDialog.dismiss();
                GlobalClass.getInstance().SnackBar(rootLayout, exception.getMessage(), -1, -1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.forgotTv:
                break;
            case R.id.loginTv:
                login(emailEt, passwordEt);
                break;
            case R.id.signUpTv:
                intent = new Intent(LoginInActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.fbTv:
                social_type = "facebook";
                LoginManager.getInstance().logInWithReadPermissions(
                        this,
                        Arrays.asList("user_photos", "email", "public_profile")

                );
                progressDialog.show();

                break;
            case R.id.googleTv:
                social_type = "google";
                signIn();
                break;
        }
    }

    private void login(EditText emailEt, EditText passwordEt) {
        email = getValueFromEdittext(emailEt);
        password = getValueFromEdittext(passwordEt);
        if (email.length() > 0 && passwordEt.length() > 0 && isValidEmailId(email)) {
            requestParams = new RequestParams();
            requestParams.put("email", email);
            requestParams.put("password", password);
            requestParams.put("devicetype", "android");
            requestParams.put("device_id", getUniqueId());
            if (GlobalClass.getInstance().isNetworkAvailable()) {
                WebReq.post(Urls.login, requestParams, new LoginInApi());
            } else {
                GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
            }
        } else {
            if (email.length() > 0) {
                if (!isValidEmailId(email)) {
                    emailEt.setError("email is Invalid");
                }
                passwordEt.setError("Password is Required");
            } else {
                passwordEt.setError("Password is Required");
            }
        }
    }

    /**
     * Rest Api of Login
     */
    public class LoginInApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
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
                    if (title.equals("Become a SELLER")) {
                        intent = new Intent(LoginInActivity.this, ShopPreferencesActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        GlobalClass.getInstance().storeUserType("1");
                        startActivity(intent);
                    } else {
                        intent = new Intent(LoginInActivity.this, BottomNavigationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        GlobalClass.getInstance().storeUserType("2");
                        startActivity(intent);
                    }

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

        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            requestParams = new RequestParams();
            requestParams.put("name", account.getDisplayName());
            requestParams.put("email", account.getEmail());
            requestParams.put("password", "123456323");
            requestParams.put("device_id", getUniqueId());
            requestParams.put("device_type", "android");
            requestParams.put("is_social", "1");
            requestParams.put("social_id", account.getId());
            requestParams.put("social_type", social_type);
            WebReq.post(Urls.signup, requestParams, new LoginInApi());
        } catch (ApiException e) {

            GlobalClass.getInstance().SnackBar(rootLayout, e.getMessage(), -1, -1);
        }
    }

    private void signIn() {
        progressDialog.show();
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 100);
    }


}
