package com.capgemini.mrchecker.webapi.httpbin;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.webapi.BasePageWebApiTest;
import com.capgemini.mrchecker.webapi.pages.httbin.RandomNBytesPage;
import io.restassured.response.Response;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RandomNBytesTest extends BasePageWebApiTest {
	private RandomNBytesPage randomNBytesPage = new RandomNBytesPage();

	@Test
	public void staticBytesTest() {
		Integer size=10;
		validateNBytesOfData(size);
	}

	@Test
	public void randomBytesTest() {
		Random generator = new Random();
		Integer size=generator.nextInt(50);
		validateNBytesOfData(size);
	}

	private void validateNBytesOfData(Integer size) {
		BFLogger.logInfo(MessageFormat.format("Step 1 - Sending GET query to {0} with valid value(size): {1}", randomNBytesPage.getEndpoint(), size));
		Response response = randomNBytesPage.sendGETQuery(Integer.toString(size));

		BFLogger.logInfo("Step 2 - Validate response status code (should be 200): ");
		assertThat(response.statusCode(), is(200));

		BFLogger.logInfo(MessageFormat.format("Step 3 - Validate response body (should be {0}): ", size));
		assertThat(response.body().asByteArray().length, is(size));
	}
}
