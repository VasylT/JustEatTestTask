package com.justeat.vasylt.testtask.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vasyl Tkachov on 27.06.2018.
 */
public class CuisineType {

    @SerializedName("Id")
    private final int id;

    @SerializedName("Name")
    private final String name;

    @SerializedName("SeoName")
    private final String seoName;

    public CuisineType(int id, String name, String seoName) {
        this.id = id;
        this.name = name;
        this.seoName = seoName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSeoName() {
        return seoName;
    }
}
