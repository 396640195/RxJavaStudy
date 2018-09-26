package com.xn.rxjava.di;

import com.xn.rxjava.service.RealmService;
import com.xn.rxjava.viewmodel.RealmServiceModelFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by xn068074 on 2018/8/24.
 */
@Module
public class RealmServiceModule {
    @Provides
    public RealmServiceModelFactory provideRealmServiceModelFactory(RealmService realmService){
        return new RealmServiceModelFactory(realmService);
    }

    @Provides
    RealmService provideRealmService() {
        return new RealmService();
    }
}
