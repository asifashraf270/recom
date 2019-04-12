package com.glowingsoft.Recomendados.Seller.Chat;

import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ChatUsersMessagesAdapter extends BaseAdapter {


    Context mContext;
    ArrayList<Users> mUsersData;
    LayoutInflater xmlInflater = null;
    Users userModel;

    TextView dateView, name, body;
    ImageView avatar;
    TimeSince timeSince;
    Typeface type;
    MediaPlayer mp;

    public ChatUsersMessagesAdapter(Context mContext, ArrayList<Users> mUsersData) {
        this.mContext = mContext;
        this.mUsersData = mUsersData;
        xmlInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
    public View getView(int position, View convertView, ViewGroup parent) {
        userModel = new Users();
        userModel = mUsersData.get(position);
        View countryItem;
        if (userModel.isMe() == true) {
            countryItem = xmlInflater.inflate(R.layout.layout_message_row_right, null);
        } else {
            countryItem = xmlInflater.inflate(R.layout.layout_message_row_left, null);
        }

        name = (TextView) countryItem.findViewById(R.id.name);
        dateView = (TextView) countryItem.findViewById(R.id.dateTV);
        dateView.setTypeface(type);
        body = (TextView) countryItem.findViewById(R.id.body);
        body.setTypeface(type);
        avatar = (ImageView) countryItem.findViewById(R.id.avatar);


        //Set Date
        String timedate = userModel.getDate().toString().trim();
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
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.HOUR, -6);
            Log.d("data", startDate.getTime() + "");
            Log.d("data1", calendar.getTime().getTime() + "");
            long timeInMilliSeconds = calendar.getTime().getTime();
            timeInMilliSeconds = timeInMilliSeconds + (3600000 * 5);
            String result = timeSince.getTimeAgo(timeInMilliSeconds, mContext);
            dateView.setText(result);

        } catch (Exception e) {

            e.printStackTrace();
            dateView.setText(userModel.getDate());
        }


//        try {
//            DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            DateFormat writeFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm aaa");
//            Date date = null;
//
//            try {
//                date = readFormat.parse(timedate);
//            } catch (ParseException e) {
//                e.printStackTrace();
//                dateView.setText(userModel.getDate());
//            }
//            String formattedDate = "";
//            if (date != null) {
//                formattedDate = writeFormat.format(date);
//            }
//            Date startDate = new SimpleDateFormat("MM-dd-yyyy hh:mm").parse(formattedDate);
//            long timeInMilliSeconds = startDate.getTime();
//            timeInMilliSeconds = timeInMilliSeconds + (3600000 * 5);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(timeInMilliSeconds);
//            dateView.setText(writeFormat.format(calendar.getTime()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//            dateView.setText(userModel.getDate());
//        }
//
//
//        Glide.with(mContext).load(userModel.getAvatar())
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.profile_place_holder))
//                .into(avatar);
        Picasso.get().load(userModel.getAvatar()).transform(new CircleTransform()).into(avatar);


        if (userModel.isMe() == true) {
            name.setText("Me");
            body.setText(" " + userModel.getBody().trim() + "      ");
            try {
                ImageView tick = (ImageView) countryItem.findViewById(R.id.tick);
                if (userModel.isTick() == true) {
                    tick.setVisibility(View.VISIBLE);


                } else {
                    tick.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            name.setText(userModel.getName() + "");
            body.setText("    " + userModel.getBody() + " ");
        }
        //return CustomRow
        return countryItem;
    }


}
