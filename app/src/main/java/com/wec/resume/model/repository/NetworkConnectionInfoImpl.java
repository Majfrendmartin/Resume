package com.wec.resume.model.repository;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectionInfoImpl implements NetworkConnectionInfo {

    private final Context context;

    public NetworkConnectionInfoImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean hasInternetConnection() {
        final ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
