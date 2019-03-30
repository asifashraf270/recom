package com.glowingsoft.Recomendados.WebReq;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.security.PublicKey;

public class WebReq {
    public static AsyncHttpClient client;

    static {
        client = new AsyncHttpClient();
    }

    public static void get(String url, RequestParams requestParams, JsonHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), requestParams, responseHandler);
    }

    public static String getAbsoluteUrl(String url) {
        return Urls.BaseUrl + url;
    }

    public static void post(String url, RequestParams requestParams, JsonHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), requestParams, responseHandler);
    }
}
