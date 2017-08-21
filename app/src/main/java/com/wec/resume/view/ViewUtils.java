package com.wec.resume.view;


import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.annimon.stream.Optional;
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

    static void loadImageToView(@NonNull Context context, @NonNull ImageView imageView,
                                @NonNull String url, @DrawableRes final int drawableRes,
                                @Nullable RequestListener<String, GlideDrawable> requestListener) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        imageView.setImageResource(drawableRes);
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

    @DrawableRes
    static int getDrawableForSectionType(SectionType sectionType) {
        switch (sectionType) {
            case EDUCATION:
                return R.drawable.ic_github;
            case JOBS:
                return R.drawable.ic_linked_in;
            case SKILLS:
                return R.mipmap.ic_social;
            case ABOUT:
                return R.mipmap.ic_launcher;
        }
        return R.mipmap.ic_launcher_round;
    }
}
