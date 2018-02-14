package com.wec.resume.model.usecase;

import com.wec.resume.model.Bio;
import com.wec.resume.model.Resume;
import com.wec.resume.model.repository.Repository;
import com.wec.resume.utils.RxJavaTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.when;

/**
 * Created by Pawel Raciborski on 14.02.2018.
 */

@RunWith(RxJavaTestRunner.class)
public class FetchBioUsecaseTest extends BaseRepositoryUsecaseTest<Bio, FetchBioUsecase> {

    private static final Bio BIO = new Bio();

    @Mock
    private Repository repository;

    private FetchBioUsecase fetchBioUsecase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        final Repository repository = getRepository();
        super.setUp(BIO, new FetchBioUsecaseImpl(repository));
        when(repository.getBio()).thenReturn(Observable.just(BIO));
    }
}