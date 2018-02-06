package com.wec.resume.presenter;

import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.model.usecase.UpdateResumeUsecase;
import com.wec.resume.view.MainActivityView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by Pawel Raciborski on 06.02.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainActivityPresenterTest {

    private MainActivityPresenter presenter;

    @Mock
    private UpdateResumeUsecase updateResumeUsecase;

    @Mock
    private FetchBioUsecase fetchBioUsecase;

    @Mock
    private MainActivityView view;

    @Before
    public void setUp() throws Exception {
        presenter = new MainActivityPresenterImpl(fetchBioUsecase, updateResumeUsecase);
    }

    @Test
    public void bioImageReadyViewBounded() throws Exception {
        presenter.bindView(view);
        presenter.bioImageReady();
        verify(view).showSplashScreen(false);
    }

    @Test
    public void bioImageReadyViewNotBounded() throws Exception {
        presenter.bioImageReady();
        verify(view, never()).showSplashScreen(false);
    }

    @Test
    public void socialsButtonClicked() throws Exception {
    }

    @Test
    public void onButtonClicked() throws Exception {
    }

}