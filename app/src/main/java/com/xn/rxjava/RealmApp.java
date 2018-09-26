package com.xn.rxjava;

import android.app.Activity;
import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xn.rxjava.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by xn068074 on 2018/7/24.
 */

public class RealmApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("realm.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(configuration);

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);

        ImageLoaderConfiguration c = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(c);

    }


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
