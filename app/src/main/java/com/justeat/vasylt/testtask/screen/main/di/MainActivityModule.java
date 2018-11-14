package com.justeat.vasylt.testtask.screen.main.di;

import dagger.Module;
import dagger.Provides;
import com.justeat.vasylt.testtask.di.ActivityModule;
import com.justeat.vasylt.testtask.di.ActivityScope;
import com.justeat.vasylt.testtask.screen.main.MainActivityPresenter;

@Module
public class MainActivityModule implements ActivityModule {

    @ActivityScope
    @Provides
    MainActivityPresenter provideMainActivityPresenter() {
        return new MainActivityPresenter();
    }

}
