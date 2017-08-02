package com.wec.resume.model.repository;



import com.wec.resume.model.Bio;
import com.wec.resume.model.Social;

import io.reactivex.Observable;

public class FakeRepositoryImpl implements Repository {

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
}
