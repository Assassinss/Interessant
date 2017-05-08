package me.zsj.interessant;

import org.junit.Test;

import me.zsj.interessant.api.DailyApi;
import me.zsj.interessant.api.InterestingApi;
import me.zsj.interessant.utils.IDUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test public void dailyApi() throws Exception {
        DailyApi dailyApi = RetrofitFactory.getRetrofit().createApi(DailyApi.class);

        assertNotNull(dailyApi);
    }

    @Test public void interestingApi() throws Exception {
        InterestingApi api = RetrofitFactory.getRetrofit().createApi(InterestingApi.class);

        assertNotNull(api);
    }

    @Test public void isDeterminedId() throws Exception {
        assertEquals(false, IDUtils.isDetermined(3));
        assertEquals(true, IDUtils.isDetermined(4));
    }
}