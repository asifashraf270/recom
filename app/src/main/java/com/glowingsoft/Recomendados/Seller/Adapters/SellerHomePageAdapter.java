package com.glowingsoft.Recomendados.Seller.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.glowingsoft.Recomendados.Buyer.Models.HomeModelClass;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.ActivitiesSeller.UpdateProductActivity;
import com.glowingsoft.Recomendados.Seller.Models.HomeSellerModel;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SellerHomePageAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<HomeSellerModel> homeModelClasses;
    ProgressDialog progressDialog;

    public SellerHomePageAdapter(Context context, List<HomeSellerModel> homeModelClasses) {
        this.context = context;
        this.homeModelClasses = homeModelClasses;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return homeModelClasses.size();
    }

    @Override
    public Object getItem(int position) {
        return homeModelClasses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.sellerhomepage_view, parent, false);
        ImageView imageView = view.findViewById(R.id.pictureIv);
        TextView priveTv = view.findViewById(R.id.priceTv);
        final TextView nameTv = view.findViewById(R.id.NameTv);
        final Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.slide_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }
        });

        priveTv.setText("" + homeModelClasses.get(position).getPrice());
        nameTv.setText("" + homeModelClasses.get(position).getTitle());
        ImageButton editBtn = view.findViewById(R.id.editBtn);
        ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);
        Picasso.get().load(homeModelClasses.get(position).getImage()).fit().placeholder(R.drawable.placeholderviewplager).into(imageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                intent.putExtra("image", homeModelClasses.get(position).getImage());
                intent.putExtra("title", homeModelClasses.get(position).getTitle());
                intent.putExtra("price", homeModelClasses.get(position).getPrice());
                intent.putExtra("categoryId", homeModelClasses.get(position).getCategory_id());
                intent.putExtra("desc", homeModelClasses.get(position).getDescription());
                intent.putExtra("id", homeModelClasses.get(position).getId());
                context.startActivity(intent);
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                intent.putExtra("image", homeModelClasses.get(position).getImage());
                intent.putExtra("title", homeModelClasses.get(position).getTitle());
                intent.putExtra("price", homeModelClasses.get(position).getPrice());
                intent.putExtra("categoryId", homeModelClasses.get(position).getCategory_id());
                intent.putExtra("desc", homeModelClasses.get(position).getDescription());
                intent.putExtra("id", homeModelClasses.get(position).getId());
                context.startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = layoutInflater.inflate(R.layout.delete_alert_dialog, null);
                Button cancelBtn, deleteBtn;
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                cancelBtn = view1.findViewById(R.id.cancelBtn);
                deleteBtn = view1.findViewById(R.id.deleteBtn);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (GlobalClass.getInstance().isNetworkAvailable()) {
                            v.startAnimation(animation);
                            RequestParams requestParams = new RequestParams();
                            requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                            requestParams.put("shop_id", GlobalClass.getInstance().returnShopId());
                            requestParams.put("product_id", homeModelClasses.get(position).getId());
                            WebReq.post(Urls.deleteProduct, requestParams, new DeleteProduct(position));
                            alertDialog.dismiss();
                        } else {
                            GlobalClass.getInstance().SnackBar(v, context.getResources().getString(R.string.networkConnection), -1, -1);
                        }
                    }
                });
                alertDialog.setView(view1);
                alertDialog.show();

            }
        });
        return view;
    }


    public class DeleteProduct extends JsonHttpResponseHandler {

        View view;
        int position;

        DeleteProduct(int pos) {
            position = pos;
        }

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
                    homeModelClasses.remove(position);
                    SellerHomePageAdapter.this.notifyDataSetChanged();
                } else {
                    GlobalClass.getInstance().SnackBar(view, "" + response.getString("message"), -1, -1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            GlobalClass.getInstance().SnackBar(view, "" + responseString, -1, -1);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }
    }


}
