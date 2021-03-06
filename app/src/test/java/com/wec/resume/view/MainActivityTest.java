package com.wec.resume.view;

import android.app.Application;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.bumptech.glide.request.RequestListener;
import com.wec.resume.BuildConfig;
import com.wec.resume.ResumeApplication;
import com.wec.resume.injection.component.ApplicationComponent;
import com.wec.resume.injection.component.DaggerApplicationComponent;
import com.wec.resume.injection.module.ApplicationModule;
import com.wec.resume.injection.module.NetworkModule;
import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.model.Social.Type;
import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.model.usecase.UpdateResumeUsecase;
import com.wec.resume.presenter.MainActivityPresenter;
import com.wec.resume.presenter.utils.UrlValidator;
import com.wec.resume.view.utils.ImageLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static com.wec.resume.model.Social.Type.GITHUB;
import static com.wec.resume.model.Social.Type.LINKED_IN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.verify;


/**
 * Created by Pawel Raciborski on 03.02.2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    public static final String AVATAR_URL = "AVATAR_URL";
    private ActivityController<MainActivity> activityController;

    @Mock
    private MainActivityPresenter presenter;

    @Mock
    private ImageLoader imageLoader;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        final ResumeApplication application = (ResumeApplication) RuntimeEnvironment.application;
        final ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .networkModule(new MockNetworkModule())
                .applicationModule(new ApplicationModule(application))
                .build();

        application.setApplicationComponent(applicationComponent);

        activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.get().setPresenterModule(new MockPresenterModule(presenter));
    }

    @After
    public void tearDown() throws Exception {
    }


    private class MockNetworkModule extends NetworkModule {
        @Override
        public ImageLoader provideImageLoader(Application application) {
            return imageLoader;
        }
    }

    private class MockPresenterModule extends PresenterModule {
        private final MainActivityPresenter presenter;

        private MockPresenterModule(MainActivityPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public MainActivityPresenter provideMainActivityPresenter(FetchBioUsecase fetchBioUsecase,
                                                                  UpdateResumeUsecase updateResumeUsecase,
                                                                  UrlValidator urlValidator) {
            return presenter;
        }
    }

    @Test
    public void presenterInjectedDuringActivityCreation() throws Exception {
        final MainActivity mainActivity = activityController.create().get();
        assertThat(mainActivity.presenter).isNotNull();
    }

    @Test
    public void splashScreenVisibleOnMainActivityResumed() throws Exception {
        final MainActivity mainActivity = getCreatedMainActivity();
        assertThat(mainActivity.ivSplashScreen.getVisibility()).isEqualTo(View.VISIBLE);
    }

    @Test
    public void splashScreenHidden() throws Exception {
        final MainActivity mainActivity = getCreatedMainActivity();
        mainActivity.showSplashScreen(false);
        assertThat(mainActivity.ivSplashScreen.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void imageLoadedIntoAppbarBackground() throws Exception {
        final MainActivity mainActivity = getCreatedMainActivity();
        assertThat(mainActivity.imageLoader).isNotNull();
        mainActivity.setAvatar(AVATAR_URL);

        verify(mainActivity.imageLoader).loadImageToView(isNotNull(), eq(AVATAR_URL), any(RequestListener.class));
    }

    private MainActivity getCreatedMainActivity() {
        return activityController.create().start().resume().get();
    }

    @Test
    public void socialsButtonClickTriggersPresenter() throws Exception {
        final MainActivity mainActivity = getCreatedMainActivity();
        mainActivity.fab.performClick();
        verify(presenter).socialsButtonClicked();
    }

    @Test
    public void githubButtonClickTriggersPresenter() throws Exception {
        final MainActivity mainActivity = getCreatedMainActivity();
        verifyFabButtonClicked(mainActivity.fabGithub, GITHUB);
    }

    @Test
    public void linkedInButtonClickTriggersPresenter() throws Exception {
        final MainActivity mainActivity = getCreatedMainActivity();
        verifyFabButtonClicked(mainActivity.fabLinkedIn, LINKED_IN);
    }

    private void verifyFabButtonClicked(FloatingActionButton button, Type buttonType) {
        button.performClick();
        verify(presenter).onButtonClicked(buttonType);
    }
}