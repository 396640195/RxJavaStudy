package com.xn.rxjava.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


import com.xn.rxjava.service.RealmService;

import javax.inject.Inject;


/**
 * Created by xn068074 on 2018/8/24.
 */

public class RealmServiceModelFactory implements ViewModelProvider.Factory  {

    private final RealmService mRealmService;
    @Inject
    public RealmServiceModelFactory(RealmService mRealmService) {
        this.mRealmService = mRealmService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new RealmServiceModel(mRealmService);
    }

}
