import com.licht.ytranslator.data.sources.CachedPreferences;
import com.licht.ytranslator.data.sources.UtilsPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class FirstTest {

    private CachedPreferences cachedPreferences;

    @Before
    public void setup() {
        cachedPreferences = new CachedPreferences();
    }

    @Test
    public void testAppPreferences() throws Exception {
        final String hello = "Hello world!";
        assertThat(hello, equalTo("Hello world!"));

    }

}
