package com.capgemini.mrchecker.test.core.unit.base.properties;

import com.capgemini.mrchecker.test.core.base.properties.PropertiesCoreTest;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.ConcurrencyUtils;
import com.google.inject.Guice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
@ResourceLock(value = "SingleThread")
public class PropertiesSettingsModuleTest {
    private static final String DEFAULT_FILE_SOURCE_FILE_PATH = System.getProperty("user.dir") + Paths.get("/src/resources/settings.properties");
    private static final String TEMP_FILE_SOURCE_FILE_PATH = System.getProperty("user.dir") + Paths.get("/src/resources/settings_temp.properties");

    public static final String[][] TEST_PROPERTIES_ARRAY = {{"core.isAnalyticsEnabled", "false"},
            {"core.isEncryptionEnabled", "false"},
            {"core.defaultEnvironmentName", "TEST_ENV"}
    };

    public static final String[][] TEST_PROPERTIES_SECOND_ARRAY = {{"core.isAnalyticsEnabled", "true"},
            {"core.isEncryptionEnabled", "true"},
            {"core.defaultEnvironmentName", "SECOND_TEST_ENV"}
    };

    public static final String TEST_PROPERTIES = buildProperties(TEST_PROPERTIES_ARRAY);
    public static final String TEST_PROPERTIES_SECOND = buildProperties(TEST_PROPERTIES_SECOND_ARRAY);

    private static String buildProperties(String[][] source) {
        final StringBuilder sb = new StringBuilder();
        Arrays.stream(source)
                .forEach(row -> sb.append(row[0])
                        .append("=")
                        .append(row[1])
                        .append('\n'));
        return sb.toString();
    }

    @BeforeAll
    public static void setUpClass() {
        PropertiesSettingsModule.delInstance();
    }

    @AfterEach
    public void tearDown() throws IOException {
        PropertiesSettingsModule.delInstance();
        if (Files.exists(Paths.get(TEMP_FILE_SOURCE_FILE_PATH))) {
            Files.move(Paths.get(TEMP_FILE_SOURCE_FILE_PATH), Paths.get(DEFAULT_FILE_SOURCE_FILE_PATH));
        }
    }

    @Test
    public void shouldInitWithDefaultInputSource() {
        assertThat(PropertiesSettingsModule.init(), is(notNullValue()));
    }

    @Test
    public void shouldInitWithCustomInputSource() {
        verifyProperties(createWithCustomInputSource(TEST_PROPERTIES), TEST_PROPERTIES_ARRAY);
    }

    private PropertiesCoreTest createWithCustomInputSource(String testProperties) {
        PropertiesSettingsModule propertiesSettingsModule = PropertiesSettingsModule.init(new ByteArrayInputStream(testProperties.getBytes()));

        return Guice.createInjector(propertiesSettingsModule)
                .getInstance(PropertiesCoreTest.class);
    }

    private void verifyProperties(PropertiesCoreTest properties, String[][] testPropertiesArray) {
        assertThat(properties.isAnalyticsEnabled(), is(equalTo(Boolean.parseBoolean(testPropertiesArray[0][1]))));
        assertThat(properties.isEncryptionEnabled(), is(equalTo(Boolean.parseBoolean(testPropertiesArray[1][1]))));
        assertThat(properties.getDefaultEnvironmentName(), is(equalTo(testPropertiesArray[2][1])));
    }

    @Test
    public void shouldInitSecondTimeAfterDelete() {
        PropertiesSettingsModule firstInstance = PropertiesSettingsModule.init();
        PropertiesSettingsModule.delInstance();
        PropertiesSettingsModule secondInstance = PropertiesSettingsModule.init();

        assertThat(firstInstance, is(not(equalTo(secondInstance))));
    }

    @Test
    public void shouldInitMultithreadedWithDifferentPropertiesSet() throws InterruptedException, ExecutionException {
        ArrayList<Callable<PropertiesCoreTest>> testTasks = new ArrayList<>();
        testTasks.add(() -> createWithCustomInputSource(TEST_PROPERTIES));
        testTasks.add(() -> createWithCustomInputSource(TEST_PROPERTIES_SECOND));

        ExecutorService executorService = Executors.newFixedThreadPool(testTasks.size());
        List<Future<PropertiesCoreTest>> properties = executorService.invokeAll(testTasks);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        verifyProperties(properties.get(0)
                .get(), TEST_PROPERTIES_ARRAY);
        verifyProperties(properties.get(1)
                .get(), TEST_PROPERTIES_SECOND_ARRAY);
    }

    @Test
    public void shouldInitMultithreaded() throws InterruptedException, ExecutionException {
        ConcurrencyUtils.getInstancesConcurrently(() -> createWithCustomInputSource(TEST_PROPERTIES))
                .forEach(s -> assertThat(s.getDefaultEnvironmentName(), is(equalTo(TEST_PROPERTIES_ARRAY[2][1]))));
    }

    @Test
    public void shouldInitThrowExceptionWhenUnparsableInput() throws IOException {
        InputStream inputStreamMock = mock(InputStream.class);
        when(inputStreamMock.read(any())).thenThrow(new IOException());

        PropertiesSettingsModule propertiesSettingsModule = PropertiesSettingsModule.init(inputStreamMock);

        assertThrows(BFInputDataException.class, () -> Guice.createInjector(propertiesSettingsModule)
                .getInstance(PropertiesCoreTest.class));
    }

    @Test
    public void shouldCreateThrowExceptionWhenFileCouldNotBeRead() throws IOException {
        Files.move(Paths.get(DEFAULT_FILE_SOURCE_FILE_PATH), Paths.get(TEMP_FILE_SOURCE_FILE_PATH));

        assertThrows(BFInputDataException.class, PropertiesSettingsModule::init);
    }
}
