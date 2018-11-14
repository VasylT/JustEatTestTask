package com.justeat.vasylt.testtask;

import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainInstrumentedTest {

    private static Actions actions;

    @BeforeClass
    public static void setUp() {
        actions = new Actions();
    }

    @Test
    public void test1_getRestaurants_ProvenPostalCode() throws InterruptedException {
        actions.getRestaurants("se19", 10);
    }

    @Test
    public void test2_getRestaurants_Kiev() throws InterruptedException {
        actions.getRestaurants("02000", 0);
    }

    @Test
    public void test3_getRestaurantsByLocation_BritainUpperwood() throws InterruptedException {
        actions.getRestaurants(51.417253,-0.104272, 10);
    }

    @AfterClass
    public static void destroy() {
        actions = null;
    }
}
