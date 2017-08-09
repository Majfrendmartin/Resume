package com.wec.resume.view;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wec.resume.R;
import com.wec.resume.injection.component.DaggerActivityComponent;
import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.model.Section.SectionType;
import com.wec.resume.presenter.DetailsActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    public void showSectionDetails(String title, String cover) {
        collapsingToolbarLayout.setTitle(title);
        Glide.with(getApplicationContext())
                .load(cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(ivToolbarParallaxBackground);
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
