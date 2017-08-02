package com.wec.resume.model.repository;



import com.wec.resume.model.Bio;

import io.reactivex.Observable;

public interface Repository {

    Observable<Bio> getBio();
}
