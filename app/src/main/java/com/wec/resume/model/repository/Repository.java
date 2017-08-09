package com.wec.resume.model.repository;


import android.support.annotation.NonNull;

import com.wec.resume.model.Bio;
import com.wec.resume.model.Resume;
import com.wec.resume.model.Section;

import java.util.Collection;

import io.reactivex.Observable;

public interface Repository {

    Observable<Bio> getBio();

    Observable<Resume> loadUpdatedResume();

    Observable<Collection<Section>> getSections();

    Observable<Section> getSectionByType(@NonNull Section.SectionType sectionType);


}
