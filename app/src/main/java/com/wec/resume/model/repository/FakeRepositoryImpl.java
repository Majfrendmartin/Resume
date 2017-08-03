package com.wec.resume.model.repository;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.wec.resume.R;
import com.wec.resume.model.Bio;
import com.wec.resume.model.Resume;
import com.wec.resume.model.Social;
import com.wec.resume.model.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static android.text.TextUtils.isEmpty;

public class FakeRepositoryImpl implements Repository {

    public static final int VERSION_INVALID = -1;
    public static final int DEFAULT_RESUME_VERSION_NUMBER = 0;
    public static final String STORED_RESUME = "STORED_RESUME";
    private final SharedPreferences preferences;
    private final NetworkAccessController networkAccessController;
    private final Context context;
    private Resume currentResume;

    public FakeRepositoryImpl(Context context, SharedPreferences preferences, NetworkAccessController networkAccessController) {
        this.context = context;
        this.preferences = preferences;
        this.networkAccessController = networkAccessController;
    }

    @Override
    public Observable<Bio> getBio() {
        return Observable.just(
                new Bio()
                        .setName("AAA")
                        .setSurname("BBB")
                        .setAvatar("http://wfiles.brothersoft.com/e6/android_189017-640x480.jpg")
                        .setSocials(new Social[]{
                                        new Social().setType(Social.Type.GITHUB).setUrl("https://github.com/Majfrendmartin"),
                                        new Social().setType(Social.Type.LINKED_IN).setUrl("https://linkedin.com/Majfrendmartin")
                                }
                        )
        );
    }

    private Observable<Version> getVersion() {
        return Observable.just(new Version().setVersion(1).setUrl("" +
                "resume_v0.json"));
    }

    private Observable<Resume> getLocalResume(@NonNull Version version) {
        return Observable.create(emitter -> {

            final int storedVersionNumber = getVersionNumber();
            if (version.getVersion() > storedVersionNumber) {
                return;
            }

            String resumeString = getStoredResumeString();
            if (isEmpty(resumeString)) {
                final InputStream inputStream = context.getResources().openRawResource(R.raw.resume_v0);
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                final StringBuilder stringBuilder = new StringBuilder();

                String line;
                try {
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    resumeString = stringBuilder.toString();

                } catch (IOException exception) {
                    exception.printStackTrace();
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
        return isEmpty(resumeString) ? null : new Gson().fromJson(resumeString, Resume.class);
    }

    @Nullable
    private String getStoredResumeString() {
        return preferences.getString(STORED_RESUME, "");
    }

    private int getVersionNumber() {
        final Resume resume = extractResume(getStoredResumeString());
        return resume != null ? resume.getVersion() : VERSION_INVALID;
    }

    private Observable<Resume> getNetworkResume(Version version) {
        final Resume resume = new Resume();
        resume.setVersion(1);
        resume.getBio().setName("NETWORK");
        return Observable.just(resume);
    }

    private Observable<Resume> getMemoryResume(Version version) {
        return Observable.create(emitter -> {
            if (version.getVersion() == getVersionNumber()) {
                emitter.onNext(this.currentResume);
            }
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Resume> loadUpdatedResume() {
        final PublishSubject<Resume> publishResult = PublishSubject.create();

        getVersion()
                .observeOn(Schedulers.io())
                .flatMapSingle(new Function<Version, SingleSource<Resume>>() {
                    @Override
                    public SingleSource<Resume> apply(@NonNull Version version) throws Exception {
                        return Observable
                                .concat(getNetworkResume(version), getLocalResume(version))
                                .first(new Resume());
                    }
                })
                .subscribe(resume -> {
                    if (resume == null) {
                        publishResult.onError(new IOException("Could not load data"));
                    }

                    currentResume = resume;

                    if (resume != extractResume(getStoredResumeString())) {
                        final SharedPreferences.Editor edit = preferences.edit();
                        edit.putString(STORED_RESUME, new Gson().toJson(resume));
                        edit.apply();
                    }
                    publishResult.onNext(resume);
                    publishResult.onComplete();
                });

        return publishResult;
    }
}
