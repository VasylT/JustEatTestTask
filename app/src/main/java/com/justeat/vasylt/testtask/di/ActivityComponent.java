package com.justeat.vasylt.testtask.di;

/**
 * Created by Vasyl Tkachov on 26.06.2018.
 */
public interface ActivityComponent<A> {
    void inject(A activity);
}
