package com.wec.resume.model.usecase;

import com.wec.resume.model.repository.Repository;

import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

/**
 * Created by Pawel Raciborski on 14.02.2018.
 */

abstract class BaseRepositoryUsecaseTest<T, Y extends Usecase<T>> {

    private T mockResultValue;

    private Y usecase;

    @Mock
    private Repository repository;

    protected void setUp(T mockResultValue, Y usecase) {
        this.mockResultValue = mockResultValue;
        this.usecase = usecase;
    }

    protected Repository getRepository() {
        return repository;
    }

    @Test
    public void repositoryMethodCalledOnExecution() throws Exception {
        final Observable<T> observable = usecase.execute();
        final TestObserver<T> testObserver = observable.test();

        testObserver.assertNoErrors();
        //noinspection unchecked
        testObserver.assertResult(mockResultValue);
    }
}
