package com.justeat.vasylt.testtask.app.dagger;

import dagger.Component;
import com.justeat.vasylt.testtask.app.ComponentsHolder;

/**
 * Created by Vasyl Tkachov on 26.06.2018.
 */
@AppScope
@Component(modules = AppModule.class)
public interface AppComponent {
    void injectComponentsHolder(ComponentsHolder componentsHolder);
}
