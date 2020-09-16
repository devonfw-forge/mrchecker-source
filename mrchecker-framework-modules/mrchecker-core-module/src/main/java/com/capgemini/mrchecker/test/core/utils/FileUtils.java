package com.capgemini.mrchecker.test.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Objects;

public class FileUtils {
	
	private FileUtils() {
	}
	
	public static String getAbsoluteFilePathFromResources(String filePathInResources) throws FileNotFoundException {
		ClassLoader loader = FileUtils.class.getClassLoader();
		URL urlFileInResources = loader.getResource(filePathInResources);
		
		if (Objects.isNull(urlFileInResources)) {
			throw new FileNotFoundException("File with relative path does not exist in resources: \"" + filePathInResources + "\"");
		}
		
		return new File(urlFileInResources.getFile()).getAbsolutePath();
	}
}
