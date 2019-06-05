package com.capgemini.mrchecker.webapi.core.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

public class HTMLParser {
	private String   originalHTML;
	private Document parsedHTML;

	public HTMLParser(String html) {
		this.originalHTML = html;
		this.parsedHTML = Jsoup.parse(html);
	}

	public static HTMLParser parse(String html) {
		return new HTMLParser(html);
	}

	public Elements getHeadingElements(int level) {
		return getTagElements("h" + level);
	}

	public int getHeadingElementsCount(int level) {
		return getElementsCount(getHeadingElements(level));
	}

	public List<String> getHeadingElementsText(int level) {
		return getElementsText(getHeadingElements(level));
	}

	public Elements getParagraphElements() {
		return getTagElements("p");
	}

	public int getParagraphElementsCount() {
		return getElementsCount(getParagraphElements());
	}

	public List<String> getParagraphElementsText() {
		return getElementsText(getParagraphElements());
	}

	public Elements getHyperlinkElements() {
		return getTagElements("a");
	}

	public int getHyperlinkElementsCount() {
		return getElementsCount(getHyperlinkElements());
	}

	public List<String> getHyperlinkElementsText() {
		return getElementsText(getHyperlinkElements());
	}

	public List<String> getHyperlinkElementsLink() {
		return getHyperlinkElements().stream().map(s -> s.attr("href")).collect(Collectors.toList());
	}

	public Elements getTagElements(String tag) {
		return this.parsedHTML.select(tag);
	}

	private List<String> getElementsText(Elements elements) {
		return elements.stream().map(Element::text).collect(Collectors.toList());
	}

	private int getElementsCount(Elements elements) {
		return elements.size();
	}
}
