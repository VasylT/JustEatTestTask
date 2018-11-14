package com.justeat.vasylt.testtask.app.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import com.justeat.vasylt.testtask.di.ActivityComponentBuilder;
import com.justeat.vasylt.testtask.screen.main.MainActivity;
import com.justeat.vasylt.testtask.screen.main.di.MainActivityComponent;

/**
 * Created by Vasyl Tkachov on 26.06.2018.
 */
@Module(subcomponents = {MainActivityComponent.class})
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @AppScope
    @Provides
    Context provideContext() {
        return context;
    }

    @Provides
    @IntoMap
    @ClassKey(MainActivity.class)
    ActivityComponentBuilder provideMainActivityBuilder(MainActivityComponent.Builder builder) {
        return builder;
    }
}
