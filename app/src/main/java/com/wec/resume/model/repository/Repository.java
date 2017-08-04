package com.wec.resume.model.repository;


import com.wec.resume.model.Bio;
import com.wec.resume.model.Resume;

import io.reactivex.Observable;

public interface Repository {

    Observable<Bio> getBio();

    Observable<Resume> loadUpdatedResume();
}
