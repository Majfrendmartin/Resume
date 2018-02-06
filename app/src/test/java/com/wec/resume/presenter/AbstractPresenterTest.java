package com.wec.resume.presenter;

import com.wec.resume.view.View;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Pawel Raciborski on 06.02.2018.
 */
public class AbstractPresenterTest {

    private class TestAbstractPresenter extends AbstractPresenter {
        //no implementation here, only for test
    }

    private TestAbstractPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new TestAbstractPresenter();
    }

    @Test
    public void getViewBeforeBinding() throws Exception {
        assertThat(presenter.getView()).isNull();
    }

    @Test
    public void isViewBounded() throws Exception {
        assertThat(presenter.isViewBounded()).isFalse();
        presenter.bindView(new View() {});
        assertThat(presenter.isViewBounded()).isTrue();
        presenter.unbindView();
        assertThat(presenter.isViewBounded()).isFalse();
    }

    @Test
    public void bindView() throws Exception {
        View view = new View() {};
        presenter.bindView(view);
        assertThat(presenter.getView()).isEqualTo(view);
    }

    @Test
    public void unbindView() throws Exception {
        presenter.unbindView();
        assertThat(presenter.getView()).isNull();
    }

}