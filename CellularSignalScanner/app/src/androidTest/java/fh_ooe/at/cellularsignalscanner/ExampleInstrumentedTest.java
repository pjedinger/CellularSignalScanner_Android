package fh_ooe.at.cellularsignalscanner;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented runnable, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under runnable.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("fh_ooe.at.cellularsignalscanner", appContext.getPackageName());
    }
}
