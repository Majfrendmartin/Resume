package com.wec.resume.injection.component;
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

import com.wec.resume.injection.module.ApplicationModule;
import com.wec.resume.injection.module.NetworkModule;
import com.wec.resume.injection.scope.PerApplication;
import com.wec.resume.model.repository.Repository;

import dagger.Component;

@PerApplication
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
    Repository repository();
}
