package com.wec.resume.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wec.resume.R;
import com.wec.resume.injection.component.DaggerActivityComponent;
import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.model.Social;
import com.wec.resume.presenter.MainActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wec.resume.model.Social.Type.GITHUB;
import static com.wec.resume.model.Social.Type.LINKED_IN;

public class MainActivity extends AbstractPresenterActivity<MainActivityPresenter> implements MainActivityView {

    private static final String PROPERTY_NAME_X = "X";
    private static final String PROPERTY_NAME_ROTATION = "rotation";
    private static final float SELECTED_ROTATION_VALUE = 135f;
    private static final float DEFAULT_ROTATION_VALUE = 0f;
    private static final int ANIMATION_TIME_MILLIS = 500;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.fab_github)
    FloatingActionButton fabGithub;

    @BindView(R.id.fab_linkedin)
    FloatingActionButton fabLinkedIn;

    @BindView(R.id.fab_container)
    RelativeLayout fabContainer;

    @BindView(R.id.iv_splash_screen)
    ImageView ivSplashScreen;

    @BindView(R.id.iv_toolbar_parallax_background)
    ImageView ivToolbarParallaxBackground;

    @BindView(R.id.collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.cl_container)
    ViewGroup clContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .presenterModule(new PresenterModule())
                .build()
                .inject(this);

        presenter.bindView(this);
        onCreateAfterInjection(savedInstanceState);
        appBarLayout.addOnOffsetChangedListener(this::updateToolbarImageAlpha);
    }

    private void updateToolbarImageAlpha(AppBarLayout appBarLayout1, float verticalOffset) {
        final int totalScrollRange = appBarLayout1.getTotalScrollRange();
        final float imageAlpha = (totalScrollRange + verticalOffset) / (float) totalScrollRange;
        ivToolbarParallaxBackground.setAlpha(imageAlpha);
    }

    @Override
    public void setTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }

    @Override
    public void setAvatar(String avatar) {
        ViewUtils.loadImageToView(this, ivToolbarParallaxBackground, avatar,
                R.mipmap.ic_launcher_round, new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        presenter.bioImageReady();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        presenter.bioImageReady();
                        return false;
                    }
                });
    }

    @Override
    public void showCouldNoteLoadDataErrorMessage() {
        Toast.makeText(this, "Could not load data", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSplashScreen(boolean showSplashScreen) {
        TransitionManager.beginDelayedTransition(clContainer);
        ivSplashScreen.setVisibility(showSplashScreen ? VISIBLE : GONE);
    }

    @Override
    public void showAndEnableSocialButtons(boolean areAvailable) {
        fabContainer.setVisibility(areAvailable ? VISIBLE : GONE);
        fab.setClickable(areAvailable);
    }

    @Override
    public void enableButtonByType(Social.Type type) {
        final FloatingActionButton button = getButtonByType(type);
        if (button != null) {
            button.setClickable(true);
            button.setVisibility(VISIBLE);
        }
    }

    @Nullable
    private FloatingActionButton getButtonByType(Social.Type type) {
        switch (type) {
            case GITHUB:
                return fabGithub;
            case LINKED_IN:
                return fabLinkedIn;
        }
        return null;
    }

    @Override
    public void animateButton(Social.Type type, int position, boolean shouldShowButton) {
        final FloatingActionButton button = getButtonByType(type);
        if (button == null) {
            return;
        }

        final int buttonMargin = getResources().getDimensionPixelSize(R.dimen.margin_10);

        final int fabWidth = fab.getWidth();
        final int buttonWidth = button.getWidth();
        final float fabX = fab.getX();
        final float startPosition = fabX + (fabWidth - buttonWidth) / 2;
        final int itemPosition = position + 1;

        final float endXValue = shouldShowButton ?
                fabX - ((buttonWidth + buttonMargin) * itemPosition) :
                startPosition;

        final ObjectAnimator objectAnimator =
                ObjectAnimator.ofFloat(button, PROPERTY_NAME_X, button.getX(), endXValue)
                        .setDuration(itemPosition * ANIMATION_TIME_MILLIS);
        objectAnimator.setInterpolator(new FastOutSlowInInterpolator());
        objectAnimator.start();
    }

    @Override
    public void setSocialButtonToSelected(boolean socialButtonSelected) {
        ObjectAnimator.ofFloat(fab, PROPERTY_NAME_ROTATION, fab.getRotation(),
                socialButtonSelected ? SELECTED_ROTATION_VALUE : DEFAULT_ROTATION_VALUE)
                .setDuration(ANIMATION_TIME_MILLIS)
                .start();
    }

    @Override
    public void navigateToURL(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @OnClick(R.id.fab)
    void fabButtonClicked() {
        presenter.socialsButtonClicked();
    }

    @OnClick(R.id.fab_github)
    void githubButtonClicked() {
        presenter.onButtonClicked(GITHUB);
    }

    @OnClick(R.id.fab_linkedin)
    void linkedInButtonClicked() {
        presenter.onButtonClicked(LINKED_IN);
    }

}
