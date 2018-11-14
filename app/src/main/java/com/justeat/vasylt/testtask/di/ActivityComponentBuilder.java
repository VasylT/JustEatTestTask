package com.justeat.vasylt.testtask.di;

/**
 * Created by Vasyl Tkachov on 26.06.2018.
 */
public interface ActivityComponentBuilder<C extends ActivityComponent, M extends ActivityModule>   {
    C build();
    ActivityComponentBuilder<C,M> module(M module);
}
