package com.capgemini.mrchecker.selenium.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class FileHelper {
	static String getAbsoluteFilePathFromResources(String filePathInResources) throws FileNotFoundException {
		ClassLoader loader = FileHelper.class.getClassLoader();
		URL urlFileInResources = loader.getResource(filePathInResources);
		if (urlFileInResources != null) {
			File file = new File(urlFileInResources.getFile());
			return file.getAbsolutePath();
		} else {
			throw new FileNotFoundException("File with relative path does not exist in resources: \"" + filePathInResources + "\"");
		}
	}
}
