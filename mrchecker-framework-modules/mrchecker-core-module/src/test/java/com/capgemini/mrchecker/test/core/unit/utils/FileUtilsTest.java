package com.capgemini.mrchecker.test.core.unit.utils;

import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
public class FileUtilsTest {

    private static final String TEST_FILE_NAME = "settings.properties";
    private static final String NO_SUCH_FILE_NAME = "NO_SUCH_FILE";

    @Test
    public void shouldReturnFilePath() throws FileNotFoundException {
        String filePath = (FileUtils.getAbsoluteFilePathFromResources(TEST_FILE_NAME));
        assertThat(filePath, containsString(TEST_FILE_NAME));
    }

    @Test
    public void shouldThrowFileNotFoundExceptionWhenNoSuchFile() {
        assertThrows(FileNotFoundException.class, () -> FileUtils.getAbsoluteFilePathFromResources(NO_SUCH_FILE_NAME));
    }
}
