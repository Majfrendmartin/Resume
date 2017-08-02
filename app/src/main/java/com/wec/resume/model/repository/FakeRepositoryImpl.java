package com.wec.resume.model.repository;
/*
 * Avaya Inc. - Proprietary (Restricted)
 * Solely for authorized persons having a need to know
 * pursuant to Company instructions.
 *
 * Copyright 2017. Avaya Inc. All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc.
 * The copyright notice above does not evidence any actual
 * or intended publication of such source code.
 */


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
