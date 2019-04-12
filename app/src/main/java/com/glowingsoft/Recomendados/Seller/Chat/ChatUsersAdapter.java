package com.glowingsoft.Recomendados.Seller.Chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mg on 2/3/2017.
 */


public class ChatUsersAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<UserChatModel> mUsersData;
    public LayoutInflater myinflator;
    UserChatModel userModel;
    TextView dateTv;
    TimeSince timeSince;

    public ChatUsersAdapter(Context mContext, ArrayList<UserChatModel> mUsersData) {
        this.mContext = mContext;
        this.mUsersData = mUsersData;
//        myinflator = LayoutInflater.from(mContext);
        myinflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        timeSince = new TimeSince();
    }


    @Override
    public int getCount() {
        return mUsersData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rootview = myinflator.inflate(R.layout.chat_user_row, null);

        userModel = new UserChatModel();
        userModel = mUsersData.get(position);

        TextView name = (TextView) rootview.findViewById(R.id.name);

        name.setText(userModel.getFriend_name() + "");


        ImageView avatar = (ImageView) rootview.findViewById(R.id.avatar);
        String userImage = userModel.getFriend_photo();
        Picasso.get().load(userImage).transform(new CircleTransform()).into(avatar);
        rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserChatMessages.class);
                intent.putExtra("user_id", mUsersData.get(position).getUser_id());
                intent.putExtra("conversation_id", mUsersData.get(position).getConversation_id());
                GlobalClass.getInstance().storeConvertionalId(mUsersData.get(position).getConversation_id());
                mContext.startActivity(intent);
            }
        });


        dateTv = (TextView) rootview.findViewById(R.id.date);


//        date.setText(userModel.getLast_message_time());

        TextView message = (TextView) rootview.findViewById(R.id.message);

        message.setText(userModel.getLast_message());


        ImageView statusdot = (ImageView) rootview.findViewById(R.id.status);

//        if (userModel.getIs_online().equals("1")) {
//            statusdot.setImageResource(R.drawable.online);
//        } else {
//            statusdot.setImageResource(R.drawable.offline);
//        }


        TextView message_count = (TextView) rootview.findViewById(R.id.message_count);
        message_count.setVisibility(View.INVISIBLE);

        String unreadMsg = userModel.getUnread().toString();
        try {
            if (Integer.valueOf(unreadMsg) > 0) {
                message_count.setVisibility(View.VISIBLE);
                message_count.setText(unreadMsg + "");
            } else {
                message_count.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Set Date
        String timedate = userModel.getLast_message_time();
        try {
            DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = readFormat.parse(timedate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = "";
            if (date != null) {
                formattedDate = writeFormat.format(date);
            }
            Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedDate);
            long timeInMilliSeconds = startDate.getTime();
            timeInMilliSeconds = timeInMilliSeconds + (3600000 * 5);
            String result = timeSince.getTimeAgo(timeInMilliSeconds, mContext);
            //  holder.date.setText("" + result);
            dateTv.setText(result);
            Log.d("date", result);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return rootview;
    }
}
