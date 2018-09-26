package com.xn.rxjava.di;



import com.xn.rxjava.ui.MVVMActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuildersModule {

	@ActivityScope
	@ContributesAndroidInjector(modules = RealmServiceModule.class)
	abstract MVVMActivity bindMVVMActivityModule();

}
