package com.wec.resume.model.usecase;



import android.support.annotation.NonNull;

import io.reactivex.Observable;

public interface Usecase<T> {
    @NonNull
    Observable<T> execute();
}
