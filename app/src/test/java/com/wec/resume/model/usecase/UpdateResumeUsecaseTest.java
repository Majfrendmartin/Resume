package com.wec.resume.model.usecase;

import com.wec.resume.model.Resume;
import com.wec.resume.model.repository.Repository;
import com.wec.resume.utils.RxJavaTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.when;

/**
 * Created by Pawel Raciborski on 14.02.2018.
 */

@RunWith(RxJavaTestRunner.class)
public class UpdateResumeUsecaseTest {

    private static final Resume RESUME = new Resume();

    @Mock
    private Repository repository;

    private UpdateResumeUsecase updateResumeUsecase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(repository.loadUpdatedResume()).thenReturn(Observable.just(RESUME));
        updateResumeUsecase = new UpdateResumeUsecaseImpl(repository);
    }

    @Test
    public void repositoryMethodCalledOnExecution() throws Exception {
        final Observable<Resume> observable = updateResumeUsecase.execute();
        final TestObserver<Resume> testObserver = observable.test();

        testObserver.assertNoErrors();
        testObserver.assertResult(RESUME);
    }
}