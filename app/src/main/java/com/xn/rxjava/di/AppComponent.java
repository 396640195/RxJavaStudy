package com.xn.rxjava.di;

import com.xn.rxjava.RealmApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by xn068074 on 2018/8/24.
 */
@Singleton
@Component(modules = {
        BuildersModule.class,
        AndroidSupportInjectionModule.class,
        RealmServiceModule.class })
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(RealmApp app);
        AppComponent build();
    }
    void inject(RealmApp app);
}
