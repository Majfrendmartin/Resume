package com.wec.resume.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

import java.util.concurrent.Semaphore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AbstractPresenterActivity<MainActivityPresenter> implements MainActivityView {

    private static final float ROTATED_POSITION = 135f;
    private static final float UNROTATED_POSITION = 0f;
    private static final int SLIDE_ANIMATION_TIME_MILLIS = 500;
    private static final int BASE_SLIDE_BUTTON_OFFSET = 180;
    private static final int NEXT_BUTTON_OFFSET = 170;
    private static final int MAX_AVAILABLE = 2;
    private final Semaphore semaphore = new Semaphore(MAX_AVAILABLE, true);
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

        int buttonOffset = BASE_SLIDE_BUTTON_OFFSET + (position * NEXT_BUTTON_OFFSET);

        // Get negative value to reverse animation.
        if (shouldShowButton) {
            buttonOffset *= -1;
        }

        //TODO: get dimensions from layout
        final ObjectAnimator animator = ObjectAnimator.ofFloat(button, "X", button.getX() + buttonOffset);
        animator.setDuration((position + 1) * SLIDE_ANIMATION_TIME_MILLIS);
        animator.setInterpolator(new FastOutSlowInInterpolator());

        animator.addListener(new BaseAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                fab.setClickable(false);
                if (shouldShowButton) {
                    button.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fab.setClickable(true);
                if (!shouldShowButton) {
                    button.setVisibility(View.INVISIBLE);
                }
            }
        });

        animator.start();
    }

    @Override
    public void setSocialButtonToSelected(boolean socialButtonSelected) {
        final ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(fab, "rotation",
                socialButtonSelected ? UNROTATED_POSITION : ROTATED_POSITION,
                socialButtonSelected ? ROTATED_POSITION : UNROTATED_POSITION);
        imageViewObjectAnimator.setDuration(500);
        imageViewObjectAnimator.start();
    }

    @OnClick(R.id.fab)
    void fabButtonClicked() {
        presenter.socialsButtonClicked();
    }

    @OnClick(R.id.fab_github)
    void githubButtonClicked() {
        presenter.githubButtonClicked();
    }

    @OnClick(R.id.fab_linkedin)
    void linkedInButtonClicked() {
        presenter.linkedInButtonClicked();
    }

}
