package com.wec.resume.model.repository;

import com.wec.resume.model.Resume;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("/resume/v")
    Observable<Integer> getCurrentResumeVersion();

    @GET("/resume/resume_v{version}.json")
    Observable<Resume> getResume(@Path("version") int version);
}
