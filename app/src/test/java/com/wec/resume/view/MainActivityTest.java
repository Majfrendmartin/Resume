package com.wec.resume.view;

import android.view.View;

import com.wec.resume.BuildConfig;
import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.model.usecase.UpdateResumeUsecase;
import com.wec.resume.presenter.MainActivityPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Pawel Raciborski on 03.02.2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    private ActivityController<MainActivity> activityController;

    @Mock
    private MainActivityPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.get().setPresenterModule(new MockPresenterModule(presenter));
    }

    @After
    public void tearDown() throws Exception {
    }

    private class MockPresenterModule extends PresenterModule {
        private final MainActivityPresenter presenter;

        private MockPresenterModule(MainActivityPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public MainActivityPresenter provideMainActivityPresenter(FetchBioUsecase fetchBioUsecase,
                                                                  UpdateResumeUsecase updateResumeUsecase) {
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

//    @Test
//    public void imageLoadedIntoAppbarBackground() throws Exception {
        //TODO: add this test after refactoring (ViewUtils ---extract--> ImageLoader)
//    }

    private MainActivity getCreatedMainActivity() {
        return activityController.create().start().resume().get();
    }
}