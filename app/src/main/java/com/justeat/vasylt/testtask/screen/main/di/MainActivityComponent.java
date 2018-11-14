package com.justeat.vasylt.testtask.screen.main.di;

import com.justeat.vasylt.testtask.di.ActivityComponent;
import com.justeat.vasylt.testtask.di.ActivityComponentBuilder;
import com.justeat.vasylt.testtask.di.ActivityScope;
import com.justeat.vasylt.testtask.screen.main.MainActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent extends ActivityComponent<MainActivity> {

    @Subcomponent.Builder
    interface Builder extends ActivityComponentBuilder<MainActivityComponent, MainActivityModule> {
    }
}
