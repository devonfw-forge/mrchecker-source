package com.capgemini.mrchecker.test.core.utils;

import javax.swing.*;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.utils.encryption.view.DataEncryptionGUI;

public class DataEncryptionApp {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			BFLogger.logInfo("Could not run app with system Look and Feel");
		}
		new DataEncryptionGUI();
	}
}
