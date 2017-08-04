package com.wec.resume.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wec.resume.R;
import com.wec.resume.injection.component.DaggerMainActivityComponent;
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

    public static final String PROPERTY_NAME_X = "X";
    public static final String PROPERTY_NAME_ROTATION = "rotation";
    private static final float ROTATED_POSITION = 135f;
    private static final float UNROTATED_POSITION = 0f;
    private static final int ANIMATION_TIME_MILLIS = 500;
    private static final int BASE_SLIDE_BUTTON_OFFSET = 180;
    private static final int NEXT_BUTTON_OFFSET = 170;
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

    @BindView(R.id.collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        DaggerMainActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .presenterModule(new PresenterModule())
                .build()
                .inject(this);

        presenter.bindView(this);
        onCreateAfterInjection(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void setTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }

    @Override
    public void setAvatar(String avatar) {
        Glide.with(MainActivity.this)
                .load(avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) findViewById(R.id.iv_toolbar_parallax_background));
    }

    @Override
    public void showCouldNoteLoadDataErrorMessage() {
        Toast.makeText(this, "Could not load data", Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideSplashScreen() {
        ivSplashScreen.setVisibility(GONE);
    }

    @Override
    public void showSplashScreen() {
        ivSplashScreen.setVisibility(VISIBLE);
    }

    @Override
    public void showAndEnableSocialButtons() {
        fabContainer.setVisibility(VISIBLE);
        fab.setClickable(true);
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

        final ObjectAnimator animator = ObjectAnimator.ofFloat(button, PROPERTY_NAME_X, button.getX(), endXValue);
        animator.setDuration(itemPosition * ANIMATION_TIME_MILLIS);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
    }

    @Override
    public void setSocialButtonToSelected(boolean socialButtonSelected) {
        final ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(fab,
                PROPERTY_NAME_ROTATION, fab.getRotation(),
                socialButtonSelected ? ROTATED_POSITION : UNROTATED_POSITION);
        imageViewObjectAnimator.setDuration(ANIMATION_TIME_MILLIS);
        imageViewObjectAnimator.start();
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
