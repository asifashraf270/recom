package com.glowingsoft.Recomendados.Seller.Chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class UserChatMessages extends AppCompatActivity {
    ListView messagesListView;
    EditText chatText;
    ImageView sendMessageButton;
    ArrayList<Users> userMessagesData;
    ChatUsersMessagesAdapter chatUsersMessagesAdapter;
    RelativeLayout rootlayout;
    Users usersModel;
    String user_id, conversionalId;
    String userMessage;
    int previousLength = 0;
    boolean mymessage = false;
    boolean gotoBottom = false;
    private MediaPlayer mp;
    ProgressDialog progressDialog;
    protected ImageView chat_back;
    TextView messagesBatch;
    int batchValue;
    Handler h;
    int delay = 5000; //5 seconds
    Runnable runnable;
    TextView name_toolbar_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_user_chat_messages);
        batchValue = GlobalClass.getInstance().returnCount();
        viewBinding();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalClass.getInstance().storeBadgeValue(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recusiveCallForMessages();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recusiveCallForMessages();

    }

    private void viewBinding() {
        messagesListView = (ListView) findViewById(R.id.messagesListView);
        chatText = (EditText) findViewById(R.id.chatText);
        sendMessageButton = (ImageView) findViewById(R.id.sendMessageBtn);
        userMessagesData = new ArrayList<>();
        messagesBatch = (TextView) findViewById(R.id.messagesBatch);
        name_toolbar_title = findViewById(R.id.name_toolbar_title);

        user_id = getIntent().getExtras().getString("user_id");
        conversionalId = getIntent().getExtras().getString("conversation_id");
        rootlayout = findViewById(R.id.rootLayout);
        chat_back = (ImageView) findViewById(R.id.chat_back);
        h = new Handler();


        progressDialog = new ProgressDialog(UserChatMessages.this);
        chatUsersMessagesAdapter = new ChatUsersMessagesAdapter(this, userMessagesData);
        messagesListView.setAdapter(chatUsersMessagesAdapter);
        chat_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        if (GlobalClass.getInstance().isNetworkAvailable()) {
            fetchChatMessages(1);
        } else {
            GlobalClass.getInstance().SnackBar(rootlayout, getResources().getString(R.string.networkConnection), -1, -1);
        }
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMessage = chatText.getText().toString();
                if (userMessage.length() == 0) {
                    //  GlobalClass.getInstance().snakbar(mRoot, "Message can't be empty", R.color.white, R.color.error);
                    Toast.makeText(UserChatMessages.this, "Message can't be empty", Toast.LENGTH_SHORT).show();

                    return;
                }

                usersModel = new Users();
                usersModel.setBody(userMessage);
                usersModel.setName("Me");
                usersModel.setDate("Sending...");
                usersModel.setMe(true);
                usersModel.setTick(false);
                userMessagesData.add(usersModel);
                previousLength = userMessagesData.size();
                chatUsersMessagesAdapter.notifyDataSetChanged();
                messagesListView.setSelection(chatUsersMessagesAdapter.getCount() - 1);
                chatText.setText("");
                sendMessages(2);
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        finish();
    }

    private void updateNewMessagesBatch() {
        try {
            batchValue = GlobalClass.getInstance().returnCount();
            if (batchValue != -1) {
                if (batchValue == 0) {
                    messagesBatch.setVisibility(View.GONE);
                } else {
                    if (batchValue > 9) {
                        messagesBatch.setVisibility(View.VISIBLE);
                        messagesBatch.setText("9+");
                    } else {
                        messagesBatch.setVisibility(View.VISIBLE);
                        messagesBatch.setText(batchValue + "");

                    }
                }


            }
        } catch (Exception e) {

        }
    }

    private void recusiveCallForMessages() {
        //start handler as activity become visible
        h.postDelayed(new Runnable() {
            public void run() {
                //do something
                fetchChatMessages(3);
                updateNewMessagesBatch();
                runnable = this;
                h.postDelayed(runnable, delay);
            }
        }, delay);
    }

    protected void fetchChatMessages(int mReqType) {
        RequestParams mParams = new RequestParams();
        //  WebReq.client.addHeader("Authorizuser", pref.getString("user_apikey", ""));
//        String id_orignal_user = pref.getString("userID", "");

        mParams.put("user_id", user_id);
        mParams.put("conversation_id", conversionalId);
//        mParams.add("conversationId", "");


//        TimeZone tz = TimeZone.getDefault();
//        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID());
//        mParams.add("timezone", tz.getID());


        WebReq.post(Urls.chat, mParams, new UserChatMessages.MyJsonHttpResponseHandler(mReqType));
    }

    protected void sendMessages(int mReqType) {
        RequestParams mParams = new RequestParams();
        //  WebReq.client.addHeader("Authorizuser", pref.getString("user_apikey", ""));

        // String id_orignal_user = pref.getString("userID", "");
        mParams.add("conversation_id", conversionalId);
        mParams.add("user_id", GlobalClass.getInstance().returnUserId());
        mParams.add("message", userMessage + "");


        if (mReqType == 2) {
            mymessage = true;
        } else {
            mymessage = false;
        }
        WebReq.post(Urls.sendMessage, mParams, new UserChatMessages.MyJsonHttpResponseHandler(mReqType));
    }

    class MyJsonHttpResponseHandler extends JsonHttpResponseHandler {
        private int mReqType;

        MyJsonHttpResponseHandler(int reqType) {
            mReqType = reqType;
        }

        @Override
        public void onStart() {
            super.onStart();
            if (mReqType == 1) {
                //mProgress.setVisibility(View.VISIBLE);
                gotoBottom = true;

            }
            if (mReqType == 2) {
                Log.d("response send message", "send message request start");
                gotoBottom = false;

            }
            if (mReqType != 2) {
                mymessage = false;
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            //  mProgress.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            // GlobalClass.getInstance().snakbar(mRoot, GlobalClass.getInstance().webError(mStatusCode, mThrow), R.color.white, R.color.error);
            // mProgress.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);

            Log.i("responseString", responseString.toString());
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {
            //  mProgress.setVisibility(View.GONE);
            Log.d("response_messages", mResponse.toString() + "");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    if (mResponse != null) {       //!mResponse.getBoolean("error")
                        if (mymessage == true) {

                            return;


                        }
                        if (mReqType == 2) {
//                            playMsgSendSound();
                        }

                        if (mReqType == 3) {
                            userMessagesData.clear();
                            gotoBottom = false;
                            mReqType = 1;
                        }
                        if (mReqType == 1) {
                            JSONArray messages = new JSONArray();
                            messages = mResponse.getJSONArray("chat");
                            for (int i = 0; i < messages.length(); i++) {
                                JSONObject eachMessage = messages.getJSONObject(i);
                                Log.d("response_each_messages", eachMessage.toString() + "");
                                usersModel = new Users();
                                usersModel.setBody(eachMessage.getString("message"));
                                usersModel.setConversation_id(eachMessage.getString("conversation_id"));
                                usersModel.setName(eachMessage.getString("sender_name"));
                                // usersModel.setRead(eachMessage.getString("read"));
//                                 usersModel.setMessage_id(eachMessage.getString("message_id"));
                                //  usersModel.setUser_id(eachMessage.getString("user_id"));
                                usersModel.setAvatar(eachMessage.getString("sender_photo"));
                                usersModel.setDate(eachMessage.getString("datetime"));
                                usersModel.setTick(true);
//                                if (usersModel.getUser_id().equals(pref.getString("userID", ""))) {
//                                    usersModel.setMe(true);
//                                }
                                String isSender = eachMessage.getString("is_sender");
                                if (isSender.equalsIgnoreCase("1")) {
                                    usersModel.setMe(true);
                                }

                                userMessagesData.add(usersModel);
                            }
                            chatUsersMessagesAdapter.notifyDataSetChanged();
                            if (gotoBottom == true) {
                                messagesListView.setSelection(chatUsersMessagesAdapter.getCount() - 1);
                            }
                            if (previousLength > 0) {

                                if (userMessagesData.size() > previousLength) {
                                    //MediaPlayer.create(mContext,R.raw.message).start();
                                    messagesListView.setSelection(chatUsersMessagesAdapter.getCount() - 1);
//                                    playMsgSendSound();
                                }
                            }
                            previousLength = userMessagesData.size();
                        }
                    } else {
                        mReqType = -0;
                        //    GlobalClass.getInstance().snakbar(mRoot, mResponse.getString("message"), R.color.white, R.color.error);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage() + "Server Error Json", Toast.LENGTH_LONG).show();
                }
            } else {


                // GlobalClass.getInstance().snakbar(mRoot, "Request is Success But Empty Data", R.color.white, R.color.error);
            }
        }
    }

    private void playMsgSendSound() {
        try {
            mp = MediaPlayer.create(this, R.raw.msg_sound);
            mp.start();

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mp.release();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


//    public class UserChatMessage extends JsonHttpResponseHandler {
//        @Override
//        public void onStart() {
//            super.onStart();
//            progressDialog.show();
//        }
//
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//            super.onSuccess(statusCode, headers, response);
//            try {
//                Log.d("response", response.toString());
//                if (response.getInt("status") == 200) {
//                    JSONArray jsonArray = response.getJSONArray("chat");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject eachMessage = jsonArray.getJSONObject(i);
//                        Log.d("response_each_messages", eachMessage.toString() + "");
//                        usersModel = new Users();
//                        usersModel.setBody(eachMessage.getString("message"));
//                        usersModel.setConversation_id(eachMessage.getString("conversation_id"));
//                        usersModel.setName(eachMessage.getString("sender_name"));
//                        // usersModel.setRead(eachMessage.getString("read"));
////                                 usersModel.setMessage_id(eachMessage.getString("message_id"));
//                        //  usersModel.setUser_id(eachMessage.getString("user_id"));
//                        usersModel.setAvatar(eachMessage.getString("sender_photo"));
//                        usersModel.setDate(eachMessage.getString("datetime"));
//                        usersModel.setTick(true);
////                                if (usersModel.getUser_id().equals(pref.getString("userID", ""))) {
////                                    usersModel.setMe(true);
////                                }
//                        String isSender = eachMessage.getString("is_sender");
//                        if (isSender.equalsIgnoreCase("1")) {
//                            usersModel.setMe(true);
//                        }
//
//                        userMessagesData.add(usersModel);
//                    }
//                    chatUsersMessagesAdapter.notifyDataSetChanged();
//
//                } else {
//                    GlobalClass.getInstance().SnackBar(rootlayout, "" + response.getString("message"), -1, -1);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//            super.onFailure(statusCode, headers, responseString, throwable);
//            progressDialog.dismiss();
//            GlobalClass.getInstance().SnackBar(rootlayout, "" + responseString.toString(), -1, -1);
//        }
//
//        @Override
//        public void onFinish() {
//            super.onFinish();
//            progressDialog.dismiss();
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalClass.getInstance().removeConId();
    }
}
