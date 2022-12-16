package com.capgemini.mrchecker.test.core.unit.analytics;

import com.capgemini.mrchecker.test.core.analytics.AnalyticsProvider;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
public class AnalyticsProviderTest {

    public static final String DEFAULT_CATEGORY = "DEFAULT_CATEGORY";

    @Test
    public void shouldDisabledAnalyticsSetInstanceDoNothing() {
        AnalyticsProvider.DISABLED.setInstance();
    }

    @Test
    public void shouldDisabledAnalyticsSendClassNameDoNothing() {
        AnalyticsProvider.DISABLED.sendClassName();
    }

    @Test
    public void shouldDisabledAnalyticsSendMethodEventDoNothing() {
        AnalyticsProvider.DISABLED.sendMethodEvent(DEFAULT_CATEGORY);
    }
}
