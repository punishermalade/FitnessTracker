package com.punisher.fitnesstracker;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.intent.IntentMonitorRegistry;

import com.punisher.fitnesstracker.geographic.GeographicService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * represents the unit test for the Geographic Service
 */
@RunWith(AndroidJUnit4.class)
public class GeographicServiceTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void getLastLocation() {
        Context c = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertNotNull(c);

        GeographicService service = new GeographicService();
        service.init(c, true);

        Location loc = service.getLastKnownPosition();
        assertNotNull(loc);

        service.disconnect();
    }
}
