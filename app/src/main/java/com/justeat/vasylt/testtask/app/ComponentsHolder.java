package com.justeat.vasylt.testtask.app;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import com.justeat.vasylt.testtask.app.dagger.AppComponent;
import com.justeat.vasylt.testtask.app.dagger.AppModule;
import com.justeat.vasylt.testtask.app.dagger.DaggerAppComponent;
import com.justeat.vasylt.testtask.di.ActivityComponent;
import com.justeat.vasylt.testtask.di.ActivityComponentBuilder;
import com.justeat.vasylt.testtask.di.ActivityModule;

/**
 * Created by Vasyl Tkachov on 26.06.2018.
 */
public class ComponentsHolder {

    private final Context context;

    @Inject
    Map<Class<?>, Provider<ActivityComponentBuilder>> builders;

    private Map<Class<?>, ActivityComponent> components;
    private AppComponent appComponent;

    ComponentsHolder(Context context) {
        this.context = context;
    }

    void init() {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(context)).build();
        appComponent.injectComponentsHolder(this);
        components = new HashMap<>();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public ActivityComponent getActivityComponent(Class<?> cls) {
        return getActivityComponent(cls, null);
    }

    public ActivityComponent getActivityComponent(Class<?> cls, ActivityModule module) {
        ActivityComponent component = components.get(cls);
        if (component == null) {
            ActivityComponentBuilder builder = builders.get(cls).get();
            if (module != null) {
                builder.module(module);
            }
            component = builder.build();
            components.put(cls, component);
        }
        return component;
    }

    public void releaseActivityComponent(Class<?> cls) {
        components.put(cls, null);

    }

}
