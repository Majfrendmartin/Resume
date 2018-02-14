package com.wec.resume.presenter;

import com.wec.resume.model.Bio;
import com.wec.resume.model.Resume;
import com.wec.resume.model.Social;
import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.model.usecase.UpdateResumeUsecase;
import com.wec.resume.presenter.utils.UrlValidator;
import com.wec.resume.utils.RxJavaTestRunner;
import com.wec.resume.view.MainActivityView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;

import static com.wec.resume.model.Social.Type.GITHUB;
import static com.wec.resume.model.Social.Type.LINKED_IN;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel Raciborski on 06.02.2018.
 */
@RunWith(RxJavaTestRunner.class)
public class MainActivityPresenterTest {

    private static final String AVATAR_URL = "AVATAR_URL";
    private MainActivityPresenter presenter;

    private static final String GITHUB_URL = "https://github.com/Majfrendmartin";
    private static final String LINKED_IN_URL = "https://www.linkedin.com/in/pawelraciborski";
    private static final Social[] SOCIALS_ARRAY = new Social[]{
            new Social().setType(GITHUB).setUrl(GITHUB_URL),
            new Social().setType(LINKED_IN).setUrl(LINKED_IN_URL)
    };
    private static final Bio BIO = new Bio().setSocials(SOCIALS_ARRAY).setAvatar(AVATAR_URL);
    private static final Resume RESUME = new Resume().setBio(BIO);

    @Mock
    private UpdateResumeUsecase updateResumeUsecase;

    @Mock
    private FetchBioUsecase fetchBioUsecase;

    @Mock
    private MainActivityView view;

    @Mock
    private UrlValidator urlValidator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(fetchBioUsecase.execute()).thenReturn(Observable.just(BIO));
        when(updateResumeUsecase.execute()).thenReturn(Observable.just(RESUME));
        when(urlValidator.isUrlValid(anyString())).thenReturn(true);

        presenter = new MainActivityPresenterImpl(fetchBioUsecase, updateResumeUsecase, urlValidator);
    }

    @Test
    public void creationTriggersResumeUpdate() throws Exception {
        presenter.onCreate(null);

        verify(updateResumeUsecase).execute();
        verify(fetchBioUsecase).execute();
    }

    @Test
    public void populateAvatarAfterDataLoaded() throws Exception {
        bindView();
        presenter.onCreate(null);

        verify(view).setAvatar(AVATAR_URL);
    }

    @Test
    public void avatarNotPopuletedAfterDataLoadedWhenViewNotBounded() throws Exception {
        presenter.onCreate(null);

        verify(view, never()).setAvatar(AVATAR_URL);
    }

    @Test
    public void handleResumeUpdateExceptionOnCreateViewBounded() throws Exception {
        when(updateResumeUsecase.execute()).thenReturn(Observable.error(new Exception()));
        bindView();
        presenter.onCreate(null);

        verify(updateResumeUsecase).execute();
        verify(fetchBioUsecase, never()).execute();
        verify(view).showCouldNoteLoadDataErrorMessage();
    }

    @Test
    public void handleResumeUpdateExceptionOnCreateViewNotBounded() throws Exception {
        when(updateResumeUsecase.execute()).thenReturn(Observable.error(new Exception()));
        presenter.onCreate(null);

        verify(updateResumeUsecase).execute();
        verify(fetchBioUsecase, never()).execute();
        verify(view, never()).showCouldNoteLoadDataErrorMessage();
    }

    @Test
    public void bioImageReadyViewBounded() throws Exception {
        bindView();
        presenter.bioImageReady();
        verify(view).showSplashScreen(false);
    }

    @Test
    public void bioImageReadyViewNotBounded() throws Exception {
        presenter.bioImageReady();
        verify(view, never()).showSplashScreen(false);
    }

    @Test
    public void githubButtonClickedViewBounded() throws Exception {
        bindView();
        presenter.setBio(BIO);
        presenter.onButtonClicked(GITHUB);
        verify(view).navigateToURL(GITHUB_URL);
    }
    @Test
    public void githubButtonClickedViewNotBounded() throws Exception {
        bindView();
        presenter.setBio(BIO);
        presenter.onButtonClicked(GITHUB);
        verify(view, never()).navigateToURL(GITHUB_URL);
    }

    @Test
    public void linkedInButtonClickedViewBounded() throws Exception {
        bindView();
        presenter.setBio(BIO);
        presenter.onButtonClicked(LINKED_IN);
        verify(view).navigateToURL(LINKED_IN_URL);
    }

    @Test
    public void linkedInButtonClickedViewNotBounded() throws Exception {
        bindView();
        presenter.setBio(BIO);
        presenter.onButtonClicked(LINKED_IN);
        verify(view, never()).navigateToURL(LINKED_IN_URL);
    }

    @Test
    public void socialButtonClickedViewBounded() throws Exception {
        bindView();
        presenter.setBio(BIO);

        presenter.socialsButtonClicked();
        verify(view).setSocialButtonToSelected(true);
        verify(view).animateButton(GITHUB, 0, true);
        verify(view).animateButton(LINKED_IN, 1, true);


        presenter.socialsButtonClicked();
        verify(view).setSocialButtonToSelected(false);
        verify(view).animateButton(GITHUB, 0, false);
        verify(view).animateButton(LINKED_IN, 1, false);
    }

    @Test
    public void socialButtonClickedViewNotBounded() throws Exception {
        presenter.setBio(BIO);

        presenter.socialsButtonClicked();
        verify(view, never()).setSocialButtonToSelected(true);
        verify(view, never()).animateButton(GITHUB, 0, true);
        verify(view, never()).animateButton(LINKED_IN, 1, true);


        presenter.socialsButtonClicked();
        verify(view, never()).setSocialButtonToSelected(false);
        verify(view, never()).animateButton(GITHUB, 0, false);
        verify(view, never()).animateButton(LINKED_IN, 1, false);
    }

    private void bindView() {
        presenter.bindView(view);
    }

}