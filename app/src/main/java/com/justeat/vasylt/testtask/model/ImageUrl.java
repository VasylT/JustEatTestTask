package com.justeat.vasylt.testtask.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vasyl Tkachov on 27.06.2018.
 */
public class ImageUrl {

    @SerializedName("StandardResolutionURL")
    private final String url;

    public ImageUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
