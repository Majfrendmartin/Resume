package com.wec.resume.model.repository;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.wec.resume.R;
import com.wec.resume.model.AboutSection;
import com.wec.resume.model.Bio;
import com.wec.resume.model.EducationSection;
import com.wec.resume.model.JobsSection;
import com.wec.resume.model.Resume;
import com.wec.resume.model.Section;
import com.wec.resume.model.SkillsSection;
import com.wec.resume.model.event.ResumeUpdatedEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Retrofit;

import static android.text.TextUtils.isEmpty;

public class RepositoryImpl implements Repository {

    private static final int VERSION_INVALID = -1;
    private static final Resume INVALID_RESUME = new Resume().setVersion(VERSION_INVALID);
    private static final String STORED_RESUME = "STORED_RESUME";
    private final SharedPreferences preferences;
    private final Context context;
    private final Api api;
    private final EventBus eventBus;
    private final Gson gson;
    private Resume currentResume;
    private Disposable loadUpdateSubscription;

    public RepositoryImpl(Context context, SharedPreferences preferences, Retrofit retrofit,
                          EventBus eventBus, Gson gson) {
        this.context = context;
        this.preferences = preferences;
        this.api = retrofit.create(Api.class);
        this.eventBus = eventBus;
        this.gson = gson;
    }

    @Override
    public Observable<Bio> getBio() {
        return currentResume != null ?
                Observable.just(currentResume.getBio()) :
                Observable.error(new NullPointerException("Resume unavailable"));
    }

    private Observable<Integer> getVersion() {
        return api.getCurrentResumeVersion().onErrorResumeNext(Observable.just(getVersionNumber()));
    }

    private Observable<Resume> getResourcesResume() {
        return Observable.create(emitter -> {
            String resumeString = getStoredResumeString();
            // Get resume from resources ONLY if there is nothing left.
            if (isEmpty(resumeString)) {
                final InputStream inputStream = context.getResources().openRawResource(R.raw.resume_v0);
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                final StringBuilder stringBuilder = new StringBuilder();

                try {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    resumeString = stringBuilder.toString();

                } catch (Exception exception) {
                    emitter.onError(exception);
                }
            }

            final Resume resume = extractResume(resumeString);
            if (resume != null) {
                emitter.onNext(resume);
            }

            emitter.onComplete();
        });
    }

    @Nullable
    private Resume extractResume(String resumeString) {
        return isEmpty(resumeString) ? null : gson.fromJson(resumeString, Resume.class);
    }

    @Nullable
    private String getStoredResumeString() {
        return preferences.getString(STORED_RESUME, "");
    }

    @Nullable
    private Resume getResume() {
        return extractResume(getStoredResumeString());
    }

    private int getVersionNumber() {
        final Resume resume = extractResume(getStoredResumeString());
        return resume != null ? resume.getVersion() : VERSION_INVALID;
    }

    @NonNull
    private Observable<Resume> getNetworkResume(int version) {
        return api.getResume(version).onErrorResumeNext(Observable.empty());
    }

    @NonNull
    private Observable<Resume> getPreferencesResume(int version) {
        return Observable.create(emitter -> {
            final Resume resume = getResume();
            if (resume != null && resume.getVersion() == version) {
                emitter.onNext(resume);
            }
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Resume> loadUpdatedResume() {
        final PublishSubject<Resume> publishResult = PublishSubject.create();

        if (loadUpdateSubscription != null && !loadUpdateSubscription.isDisposed()) {
            loadUpdateSubscription.dispose();
        }

        loadUpdateSubscription = getVersion()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .flatMapSingle(this::createUpdateResumeSingle)
                .subscribe(resume -> handleUpdateResumeResult(publishResult, resume));

        return publishResult;
    }

    @Override
    public Observable<Collection<Section>> getSections() {
        return Observable.create(emitter -> {
            if (currentResume == null) {
                emitter.onError(new NullPointerException("No resume data available"));
                emitter.onComplete();
                return;
            }

            final List<Section> items = getSectionsList();

            emitter.onNext(items);
            emitter.onComplete();
        });
    }

    @NonNull
    private List<Section> getSectionsList() {
        final List<Section> items = new ArrayList<>(3);

        final EducationSection educationSection = currentResume.getEducation();
        if (educationSection != null) {
            items.add(educationSection);
        }

        final JobsSection jobsSection = currentResume.getJobs();
        if (jobsSection != null) {
            items.add(jobsSection);
        }

        final SkillsSection skillsSection = currentResume.getSkills();
        if (skillsSection != null) {
            items.add(skillsSection);
        }

        final AboutSection aboutSection = currentResume.getAbout();
        if (aboutSection != null) {
            items.add(aboutSection);
        }
        return items;
    }

    @Override
    public Observable<Section> getSectionByType(@android.support.annotation.NonNull Section.SectionType sectionType) {
        return Observable.create(emitter -> {
            if (currentResume == null) {
                emitter.onError(new NullPointerException("No resume data available"));
                emitter.onComplete();
                return;
            }

            Section selectedSection;

            switch (sectionType) {
                case EDUCATION:
                    selectedSection = currentResume.getEducation();
                    break;
                case JOBS:
                    selectedSection = currentResume.getJobs();
                    break;
                case SKILLS:
                    selectedSection = currentResume.getSkills();
                    break;
                case ABOUT:
                    selectedSection = currentResume.getAbout();
                    break;
                default:
                    selectedSection = null;
            }

            emitter.onNext(selectedSection);
            emitter.onComplete();
        });
    }

    private void handleUpdateResumeResult(PublishSubject<Resume> publishResult, Resume resume) {
        if (resume == null || resume.getVersion() == VERSION_INVALID) {
            publishResult.onError(new IOException("Could not load data"));
        }

        currentResume = resume;

        if (resume != extractResume(getStoredResumeString())) {
            final SharedPreferences.Editor edit = preferences.edit();
            edit.putString(STORED_RESUME, gson.toJson(resume));
            edit.apply();
        }

        eventBus.postSticky(new ResumeUpdatedEvent());

        publishResult.onNext(resume);
        publishResult.onComplete();
    }

    private Single<Resume> createUpdateResumeSingle(Integer version) {
        return Observable
                .concat(getPreferencesResume(version),
                        getNetworkResume(version),
                        getResourcesResume())
                .first(INVALID_RESUME);
    }
}
