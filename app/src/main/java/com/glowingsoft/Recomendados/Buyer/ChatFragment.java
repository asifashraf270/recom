package com.glowingsoft.Recomendados.Buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.Chat.ChatUsersAdapter;
import com.glowingsoft.Recomendados.Seller.Chat.UserChatMessages;
import com.glowingsoft.Recomendados.Seller.Chat.UserChatModel;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ChatFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private RelativeLayout rootLayout;
    private ArrayList<UserChatModel> mUsersData;
    private ChatUsersAdapter chatUsersAdapter;
    private UserChatModel userchatmodel;
    private Runnable runnableCode;
    private Handler handler = new Handler();
    String conversation_id, user_id, friend_id, user_name, friend_name, last_message, last_message_time, friend_photo, unread;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        try {
            if (getArguments().getString("check").length() > 0) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        Intent intent = new Intent(getActivity(), UserChatMessages.class);
                        intent.putExtra("user_id", getArguments().getString("user_id"));
                        intent.putExtra("conversation_id", getArguments().getString("conversation_id"));
                        startActivity(intent);
                    }
                }, 3000);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewBinding(view);
        return view;
    }

    private void viewBinding(View view) {
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        listView = view.findViewById(R.id.chatUsersList);
        rootLayout = view.findViewById(R.id.rootLayout);
        mUsersData = new ArrayList<>();
        chatUsersAdapter = new ChatUsersAdapter(getActivity(), mUsersData);
        listView.setAdapter(chatUsersAdapter);
        runConversationTHread();
    }

    private void runConversationTHread() {
        runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Handlers", "Called on main In conversaation Activity");

                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    OnlineUsersRequest();
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, "" + getResources().getString(R.string.networkConnection), -1, -1);
                }
                handler.postDelayed(runnableCode, 5000);
            }
        };

        handler.post(runnableCode);

    }


    protected void OnlineUsersRequest() {


        RequestParams mParams = new RequestParams();
        mParams.put("user_id", GlobalClass.getInstance().returnUserId());

        // WebReq.client.addHeader("accessToken", accessToken);
        WebReq.post(Urls.conversations, mParams, new ConversionRestApi());
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnableCode);

    }

    public class ConversionRestApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            mUsersData.clear();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getInt("status") == 200) {

                    JSONArray jsonArray = response.getJSONArray("conversations");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        conversation_id = object.getString("conversation_id");
                        user_id = object.getString("user_id");
                        friend_id = object.getString("friend_id");
                        user_name = object.getString("user_name");
                        friend_name = object.getString("friend_name");
                        last_message = object.getString("last_message");
                        last_message_time = object.getString("last_message_time");
                        friend_photo = object.getString("friend_photo");
                        unread = object.getString("unread");
                        userchatmodel = new UserChatModel();
                        userchatmodel.setConversation_id(conversation_id);
                        userchatmodel.setUser_id(user_id);
                        userchatmodel.setFriend_id(friend_id);
                        userchatmodel.setUser_name(user_name);
                        userchatmodel.setFriend_name(friend_name);
                        userchatmodel.setLast_message(last_message);
                        userchatmodel.setLast_message_time(last_message_time);
                        userchatmodel.setFriend_photo(friend_photo);
                        userchatmodel.setUnread(unread);
                        mUsersData.add(userchatmodel);
                    }
                    chatUsersAdapter.notifyDataSetChanged();

                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, "" + response.getString("message"), -1, -1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            GlobalClass.getInstance().SnackBar(rootLayout, "" + responseString, -1, -1);
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    }


}
