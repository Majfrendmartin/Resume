package com.wec.resume.model.repository;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkAccessControllerImpl implements NetworkAccessController {

    private final Context context;


    public NetworkAccessControllerImpl(Context context) {
        this.context = context;
    }

    private boolean isMarshmallowOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

//    @Override
//    public boolean isInternetAccessGranted() {
//        return false;
//    }
//TODO: Handle permissions for Marshmellow and later
//    @Override
//    public void requestInternetPermission() {
//        if (isMarshmallowOrLater()) {
//            ActivityCompat.requestPermissions(activity,
//                    new String[]{Manifest.permission.INTERNET},
//                    INTERNET_PERMISSION_REQUEST_CODE);
//        }
//    }

    @Override
    public boolean isInternetConnectionAvailable() {
        final ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
