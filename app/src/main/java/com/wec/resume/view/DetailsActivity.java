package com.wec.resume.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wec.resume.R;
import com.wec.resume.injection.component.DaggerActivityComponent;
import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.model.Section.SectionType;
import com.wec.resume.presenter.DetailsActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.ImageView.ScaleType.FIT_CENTER;
import static com.wec.resume.view.MainActivityFragment.KEY_EXTRA_SELECTED_TYPE;

public class DetailsActivity extends AbstractPresenterActivity<DetailsActivityPresenter> implements DetailsActivityView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_toolbar_parallax_background)
    ImageView ivToolbarParallaxBackground;

    @BindView(R.id.collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        supportPostponeEnterTransition();
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .presenterModule(new PresenterModule())
                .build()
                .inject(this);

        presenter.bindView(this);
        presenter.setSectionType(
                (SectionType) getIntent().getSerializableExtra(KEY_EXTRA_SELECTED_TYPE));
        onCreateAfterInjection(savedInstanceState);
    }

    @Override
    public void showSectionDetails(String cover, SectionType sectionType) {
        ViewUtils.loadImageToView(this, ivToolbarParallaxBackground, cover,
                ViewUtils.getDrawableForSectionType(sectionType), new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target,
                                               boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        ivToolbarParallaxBackground.setScaleType(FIT_CENTER);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                }, palette -> {
                    final Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch == null) {
                        return;
                    }

                    @SuppressLint("PrivateResource")
                    final Drawable backArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material, getTheme());
                    backArrow.setColorFilter(swatch.getTitleTextColor(), PorterDuff.Mode.SRC_ATOP);
                    try {
                        getSupportActionBar().setHomeAsUpIndicator(backArrow);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void performUpNavigation() {
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.homeButtonClicked();
        }
        return super.onOptionsItemSelected(item);
    }
}
