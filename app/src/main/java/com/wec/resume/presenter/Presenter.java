package com.wec.resume.presenter;



import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wec.resume.view.View;

public interface Presenter<T extends View> {
    T getView();

    boolean isViewBounded();

    void bindView(final T view);

    void unbindView();

    void onCreate(@Nullable Bundle bundle);

    void onDestroy();

    void onStart();

    void onStop();

    void onResume();

    void onPause();

    void onSaveInstanceState(final Bundle bundle);
}
