package com.onetoall.yjt;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals("android_version_new",getID("api/version/android_version_new"));
    }

    private boolean isReallyMoney(String money) {

        return money.matches("^(([1-9]\\d{0,5})|0)(\\.\\d{0,2})?$");
    }

    String getID(String url){
        int index = url.lastIndexOf("/");

        String after = url.substring(index + 1);

        return after;
    }

}