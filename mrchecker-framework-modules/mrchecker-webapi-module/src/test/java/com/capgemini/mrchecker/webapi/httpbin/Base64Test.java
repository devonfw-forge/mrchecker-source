package com.capgemini.mrchecker.webapi.httpbin;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.webapi.BasePageWebApiTest;
import com.capgemini.mrchecker.webapi.pages.httbin.Base64Page;
import io.restassured.response.Response;
import org.junit.Test;

import java.text.MessageFormat;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class Base64Test extends BasePageWebApiTest {
	private Base64Page base64Page = new Base64Page();

	@Test
	public void validateCorrectnessOfBase64Decode_AwesomeText() {
		String validBase64Data = "SFRUUEJJTiBpcyBhd2Vzb21l";
		String validBase64Decode = "HTTPBIN is awesome";
		base64DecodeDataCheck(validBase64Data, validBase64Decode);
	}

	@Test
	public void validateCorrectnessOfBase64Decode_MrCheckerText() {
		String validBase64Data = "TXIuQ2hlY2tlcg==";
		String validBase64Decode = "Mr.Checker";
		base64DecodeDataCheck(validBase64Data, validBase64Decode);
	}

	private void base64DecodeDataCheck(String base64Data, String expectedResult) {
		BFLogger.logInfo(MessageFormat.format("Step 1 - Sending GET query to {0} with valid base 64 data: {1}", base64Page.getEndpoint(), base64Data));
		Response response = base64Page.sendGETQuery(base64Data);

		BFLogger.logInfo("Step 2 - Validate response status code (should be 200): ");
		assertThat(response.statusCode(), is(200));

		BFLogger.logInfo(MessageFormat.format("Step 3 - Validate response body (should be {0}): ", expectedResult));
		assertThat(response.body().asString(), is(expectedResult));
	}
}