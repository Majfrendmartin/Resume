package com.wec.resume.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.widget.ImageView;

import com.annimon.stream.Optional;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wec.resume.R;
import com.wec.resume.model.Section.SectionType;

final class ViewUtils {
    private ViewUtils() {
    }

    static void loadImageToView(@NonNull Context context, @NonNull ImageView imageView, @NonNull String url,
                                @Nullable RequestListener<String, GlideDrawable> requestListener) {
        loadImageToView(context, imageView, url, -1, false, requestListener);
    }

    static void loadImageToView(@NonNull Context context, @NonNull ImageView imageView,
                                @NonNull String url, @DrawableRes final int drawableRes,
                                @Nullable RequestListener<String, GlideDrawable> requestListener) {
        loadImageToView(context, imageView, url, drawableRes, true, requestListener);
    }

    private static void loadImageToView(@NonNull Context context, @NonNull ImageView imageView,
                                @NonNull String url, @DrawableRes final int offlinePlaceholder,
                                boolean hasPlaceholder,
                                @Nullable RequestListener<String, GlideDrawable> requestListener) {
        initGlide(context, url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        if (hasPlaceholder) {
                            imageView.setImageResource(offlinePlaceholder);
                        }
                        Optional.ofNullable(requestListener)
                                .ifPresent(rl -> rl.onException(e, model, target, isFirstResource));
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target, boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        Optional.ofNullable(requestListener)
                                .ifPresent(rl -> rl.onResourceReady(
                                        resource, model, target, isFromMemoryCache, isFirstResource));
                        return false;
                    }
                })
                .into(imageView);
    }

    static void loadImageToView(@NonNull Context context, @NonNull ImageView imageView, @NonNull String url) {
        initGlide(context, url).into(imageView);
    }

    private static DrawableRequestBuilder<String> initGlide(@NonNull Context context, @NonNull String url) {
        return Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    static void loadImageToView(@NonNull Context context, @NonNull ImageView imageView,
                                @NonNull String url, @DrawableRes int drawableRes,
                                @Nullable RequestListener<String, Bitmap> requestListener,
                                @Nullable PaletteAsyncListener paletteAsyncListener) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(
                        new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target,
                                                       boolean isFirstResource) {
                                imageView.setImageResource(drawableRes);
                                Optional.ofNullable(requestListener)
                                        .ifPresent(rl -> rl.onException(e, model, target, isFirstResource));
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target,
                                                           boolean isFromMemoryCache, boolean isFirstResource) {
                                Optional.ofNullable(requestListener)
                                        .ifPresent(rl -> rl.onResourceReady(
                                                resource, model, target, isFromMemoryCache, isFirstResource));
                                Optional.ofNullable(paletteAsyncListener)
                                        .ifPresent(pl -> Palette.from(resource).generate(pl));

                                return false;
                            }
                        }
                )
                .into(imageView);
    }

    @DrawableRes
    static int getDrawableForSectionType(SectionType sectionType) {
        switch (sectionType) {
            case EDUCATION:
                return R.drawable.ic_education;
            case JOBS:
                return R.drawable.ic_work;
            case SKILLS:
                return R.drawable.ic_skill;
            case ABOUT:
                return R.drawable.ic_about;
        }
        return R.mipmap.ic_launcher_round;
    }
}
