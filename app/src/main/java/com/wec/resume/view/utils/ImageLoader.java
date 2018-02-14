package com.wec.resume.view.utils;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;

/**
 * Created by Pawel Raciborski on 09.02.2018.
 */

public interface ImageLoader {
    void loadImageToView(@NonNull ImageView imageView, @NonNull String url,
                         @Nullable RequestListener<String, GlideDrawable> requestListener);

    void loadImageToView(@NonNull ImageView imageView,
                         @NonNull String url, @DrawableRes final int drawableRes,
                         @Nullable RequestListener<String, GlideDrawable> requestListener);

    void loadImageToView(@NonNull ImageView imageView, @NonNull String url);

    void loadImageToView(@NonNull ImageView imageView,
                         @NonNull String url, @DrawableRes int drawableRes,
                         @Nullable RequestListener<String, Bitmap> requestListener,
                         @Nullable Palette.PaletteAsyncListener paletteAsyncListener);
}
