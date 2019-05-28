package com.capgemini.mrchecker.webapi.httpbin;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.webapi.BasePageWebApiTest;
import com.capgemini.mrchecker.webapi.pages.httbin.HtmlPage;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HtmlTest extends BasePageWebApiTest {
	private HtmlPage htmlPage = new HtmlPage();

	@Test
	public void sendSimpleGETQuery() {
		BFLogger.logInfo("Step 1 - Sending GET query to " + htmlPage.getEndpoint());
		Response response = htmlPage.getHtmlDocument();

		BFLogger.logInfo("Step 2 - Validate response status code: ");
		assertThat(response.statusCode(), is(200));

		BFLogger.logInfo("Step 3 - Validate response body is html that contains proper h1 tag content (count and value)");
		ResponseBody body = response.body();
		String htmlText = body.asString();
		Document doc = Jsoup.parse(htmlText);
		Elements h1 = doc.select("h1");
		assertThat(h1.size(), is(1));
		assertThat(h1.get(0).text(), is("Herman Melville - Moby-Dick"));

		BFLogger.logInfo("Step 4 - Validate response body is html that contains proper p tag content (count and length)");
		Elements p = doc.select("p");
		assertThat(p.size(), is(1));
		assertThat(p.get(0).text().length(), is(3566));
	}
}
