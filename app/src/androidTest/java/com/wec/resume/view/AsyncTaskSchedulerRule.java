package com.wec.resume.view;

import android.os.AsyncTask;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Pawel Raciborski on 28.02.2018.
 */

public class AsyncTaskSchedulerRule implements TestRule {

    final Scheduler asyncTaskScheduler = Schedulers.from(AsyncTask.SERIAL_EXECUTOR);

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> asyncTaskScheduler);
                RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> asyncTaskScheduler);
                RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> asyncTaskScheduler);
                RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> asyncTaskScheduler);
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> asyncTaskScheduler);
                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }
}
