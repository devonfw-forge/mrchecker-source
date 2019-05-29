package com.capgemini.mrchecker.webapi.httpbin;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.webapi.BasePageWebApiTest;
import com.capgemini.mrchecker.webapi.pages.httbin.LinksPage;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.text.MessageFormat;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HtmlLinksTest extends BasePageWebApiTest {
	private LinksPage linksPage = new LinksPage();

	@Test
	public void notFoundOnNegativeNParam() {
		int n = -1;
		int offset = 5;
		BFLogger.logInfo(MessageFormat.format("Step 1 - Sending GET query to {0} with negative n param: {1} and positive offset: {2}", linksPage.getEndpoint(), n, offset));
		Response response = linksPage.getHtmlDocument(n, offset);

		BFLogger.logInfo("Step 2 - Validate response status code (should be 404): ");
		assertThat(response.statusCode(), is(404));
	}

	@Test
	public void notFoundOnNegativeOffsetParam() {
		int n = 5;
		int offset = -1;
		BFLogger.logInfo(MessageFormat.format("Step 1 - Sending GET query to {0} with positive n param: {1} and negative offset: {2}", linksPage.getEndpoint(), n, offset));
		Response response = linksPage.getHtmlDocument(n, offset);

		BFLogger.logInfo("Step 2 - Validate response status code (should be 404): ");
		assertThat(response.statusCode(), is(404));
	}

	@Test
	public void offsetBiggerThanNParam() {
		int n = 2;
		int offset = 5;
		BFLogger.logInfo(MessageFormat.format("Step 1 - Sending GET query to {0} with n param: {1} and offset: {2}", linksPage.getEndpoint(), n, offset));
		Response response = linksPage.getHtmlDocument(n, offset);

		BFLogger.logInfo("Step 2 - Validate response status code (should be 200): ");
		assertThat(response.statusCode(), is(200));

		BFLogger.logInfo("Step 3 - Validate links in html response - count");
		ResponseBody body = response.body();
		String htmlText = body.asString();
		Document doc = Jsoup.parse(htmlText);
		Elements links = doc.select("a");
		assertThat(links.size(), is(n));

		BFLogger.logInfo("Step 4 - Validate links in html response - text");
		for (int i = 0; i < links.size(); i++) {
			Element link = links.get(i);
			assertThat(link.text(), is(String.valueOf(i)));
		}

		BFLogger.logInfo("Step 5 - Validate links in html response - href attribute");
		for (int i = 0; i < links.size(); i++) {
			Element link = links.get(i);
			assertThat(link.attr("href"), is("/links/" + n + "/" + i));
		}

		BFLogger.logInfo("Step 6 - Validate that there is no text other than links");
		assertThat(doc.select("body").first().ownText(), is(""));
	}

	@Test
	public void offsetLowerThanNParam() {
		int n = 10;
		int offset = 5;
		BFLogger.logInfo(MessageFormat.format("Step 1 - Sending GET query to {0} with n param: {1} and offset: {2}", linksPage.getEndpoint(), n, offset));
		Response response = linksPage.getHtmlDocument(n, offset);

		BFLogger.logInfo("Step 2 - Validate response status code (should be 200): ");
		assertThat(response.statusCode(), is(200));

		BFLogger.logInfo("Step 3 - Validate links in html response - count");
		ResponseBody body = response.body();
		String htmlText = body.asString();
		Document doc = Jsoup.parse(htmlText);
		Elements links = doc.select("a");
		assertThat(links.size(), is(n - 1));

		BFLogger.logInfo("Step 4 - Validate links in html response - text");
		for (int i = 0; i < links.size(); i++) {
			Element link = links.get(i);
			int j = i;
			if (i >= offset) {
				j++;
			}
			assertThat(link.text(), is(String.valueOf(j)));
		}

		BFLogger.logInfo("Step 5 - Validate links in html response - href attribute");
		for (int i = 0; i < links.size(); i++) {
			Element link = links.get(i);
			int j = i;
			if (i >= offset) {
				j++;
			}
			assertThat(link.attr("href"), is("/links/" + n + "/" + j));
		}

		BFLogger.logInfo(MessageFormat.format("Step 6 - Validate that there is text equal to offset: {0}", offset));
		assertThat(doc.select("body").first().ownText(), is(String.valueOf(offset)));
	}
}