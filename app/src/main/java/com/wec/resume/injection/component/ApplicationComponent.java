package com.wec.resume.injection.component;


import com.wec.resume.injection.module.ApplicationModule;
import com.wec.resume.injection.module.NetworkModule;
import com.wec.resume.injection.scope.PerApplication;
import com.wec.resume.model.repository.NetworkConnectionInfo;
import com.wec.resume.model.repository.Repository;
import com.wec.resume.view.utils.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import dagger.Component;

@PerApplication
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
    Repository repository();

    EventBus eventBus();

    NetworkConnectionInfo networkConnectionInfo();

    ImageLoader imageLoader();
}
