package com.wec.resume.view.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.annimon.stream.Optional;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wec.resume.R;
import com.wec.resume.model.Section;

/**
 * Created by Pawel Raciborski on 09.02.2018.
 */

public class ImageLoaderImpl implements ImageLoader {

    private final Context context;

    public ImageLoaderImpl(Context context) {
        this.context = context;
    }

    @Override
    public void loadImageToView(@NonNull ImageView imageView, @NonNull String url,
                                @Nullable RequestListener<String, GlideDrawable> requestListener) {
        loadImageToView(imageView, url, -1, false, requestListener);
    }

    @Override
    public void loadImageToView(@NonNull ImageView imageView, @NonNull String url,
                                @DrawableRes final int drawableRes,
                                @Nullable RequestListener<String, GlideDrawable> requestListener) {
        loadImageToView(imageView, url, drawableRes, true, requestListener);
    }

    private void loadImageToView(@NonNull ImageView imageView, @NonNull String url,
                                 @DrawableRes final int offlinePlaceholder, boolean hasPlaceholder,
                                 @Nullable RequestListener<String, GlideDrawable> requestListener) {
        initGlide(url)
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
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        Optional.ofNullable(requestListener)
                                .ifPresent(rl -> rl.onResourceReady(
                                        resource, model, target, isFromMemoryCache, isFirstResource));
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void loadImageToView(@NonNull ImageView imageView, @NonNull String url) {
        initGlide(url).into(imageView);
    }

    private DrawableRequestBuilder<String> initGlide(@NonNull String url) {
        return Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public void loadImageToView(@NonNull ImageView imageView,
                                @NonNull String url, @DrawableRes int drawableRes,
                                @Nullable RequestListener<String, Bitmap> requestListener,
                                @Nullable Palette.PaletteAsyncListener paletteAsyncListener) {
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
    int getDrawableForSectionType(Section.SectionType sectionType) {
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
