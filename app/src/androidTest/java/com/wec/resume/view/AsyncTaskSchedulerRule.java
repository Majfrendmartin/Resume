package com.wec.resume.view;

import com.squareup.rx2.idler.Rx2Idler;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by Pawel Raciborski on 28.02.2018.
 */

public class AsyncTaskSchedulerRule implements TestRule {


    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                RxJavaPlugins.setInitComputationSchedulerHandler(Rx2Idler.create("RxJava 2.x Computation Scheduler"));
                RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create("RxJava 2.x Computation Scheduler"));
                RxJavaPlugins.setInitNewThreadSchedulerHandler(Rx2Idler.create("RxJava 2.x Computation Scheduler"));
                RxJavaPlugins.setInitSingleSchedulerHandler(Rx2Idler.create("RxJava 2.x Computation Scheduler"));
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(Rx2Idler.create("RxJava 2.x Computation Scheduler"));
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
