package com.capgemini.mrchecker.test.core.utils;

import javax.swing.*;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.DataEncryptionController;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.DataEncryptionControllerImp;
import com.capgemini.mrchecker.test.core.utils.encryption.view.DataEncryptionGUI;
import com.capgemini.mrchecker.test.core.utils.encryption.view.DataEncryptionView;

public class DataEncryptionApp {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			BFLogger.logError("Could not run app with system Look and Feel");
		}
		DataEncryptionController dataEncryptionController = new DataEncryptionControllerImp();
		DataEncryptionView dataEncryptionView = new DataEncryptionGUI(dataEncryptionController);
		dataEncryptionController.setView(dataEncryptionView);
	}
}
