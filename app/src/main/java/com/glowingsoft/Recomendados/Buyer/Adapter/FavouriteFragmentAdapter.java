package com.glowingsoft.Recomendados.Buyer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.Buyer.Activities.DetailActivity;
import com.glowingsoft.Recomendados.Buyer.Models.HomeModelClass;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FavouriteFragmentAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    List<HomeModelClass> modelClasses;
    View parentLayout;

    public FavouriteFragmentAdapter(Context context, List<HomeModelClass> modelClasses) {
        this.context = context;
        this.modelClasses = modelClasses;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return modelClasses.size();
    }

    @Override
    public Object getItem(int position) {
        return modelClasses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        parentLayout = parent;
        View view = layoutInflater.inflate(R.layout.home_buyer_view, parent, false);
        ImageView pictureIv = view.findViewById(R.id.pictureIv);
        TextView price = view.findViewById(R.id.priceTv);
        TextView description = view.findViewById(R.id.description);
        TextView categoryName = view.findViewById(R.id.NameTv);
        final SparkButton favouriteBtn = view.findViewById(R.id.favouriteBtn);
        price.setText(modelClasses.get(position).getPrice());
        description.setText(modelClasses.get(position).getTitle());
        categoryName.setText(modelClasses.get(position).getCategory_title());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(context, DetailActivity.class);
                intent.putExtra("product_id", modelClasses.get(position).getId());
                context.startActivity(intent);
            }
        });
        if (modelClasses.get(position).getIs_favorite().equals("true")) {
            favouriteBtn.setChecked(true);
        }

        favouriteBtn.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                    requestParams.put("product_id", modelClasses.get(position).getId());
                    WebReq.post(Urls.like, requestParams, new FavouriteFragmentAdapter.LikeUnLikeRestApi());
                } else {
                    GlobalClass.getInstance().SnackBar(parent, context.getResources().getString(R.string.networkConnection), -1, -1);
                }
                if (!buttonState) {
                    modelClasses.remove(position);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });


        Picasso.get()
                .load(modelClasses.get(position).getImage())
                .fit()
                .placeholder(R.drawable.placeholderviewplager)
                .into(pictureIv, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        return view;
    }

    public class LikeUnLikeRestApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getInt("status") == 200) {

                } else {
                    GlobalClass.getInstance().SnackBar(parentLayout, response.getString("message"), -1, -1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    }
}
