package com.wec.resume.injection.component;


import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.injection.module.UsecaseModule;
import com.wec.resume.injection.scope.PerActivity;
import com.wec.resume.view.DetailsActivity;
import com.wec.resume.view.DetailsActivityFragment;
import com.wec.resume.view.MainActivity;
import com.wec.resume.view.MainActivityFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {PresenterModule.class, UsecaseModule.class})
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(MainActivityFragment mainActivityFragment);
    void inject(DetailsActivity detailsActivity);
    void inject(DetailsActivityFragment detailsActivityFragment);
}
