package com.capgemini.mrchecker.jemmy.utils;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.netbeans.jemmy.ClassReference;

import java.lang.reflect.InvocationTargetException;

public class GUIBrowserApp {

	public void start() {
		try {
			new ClassReference("org.netbeans.jemmy.explorer.GUIBrowser").startApplication();
		} catch (InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
			BFLogger.logError("Could not start GUIBrowser app");
		}
	}
}
