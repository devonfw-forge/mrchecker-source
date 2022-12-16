package com.capgemini.mrchecker.test.core.utils;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.DataEncryptionController;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.IDataEncryptionController;
import com.capgemini.mrchecker.test.core.utils.encryption.view.DataEncryptionGUI;
import com.capgemini.mrchecker.test.core.utils.encryption.view.IDataEncryptionView;

import javax.swing.*;

public class DataEncryptionApp {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            BFLogger.logError("Could not run app with system Look and Feel");
        }
        IDataEncryptionController IDataEncryptionController = new DataEncryptionController();
        IDataEncryptionView IDataEncryptionView = new DataEncryptionGUI(IDataEncryptionController);
        IDataEncryptionController.setView(IDataEncryptionView);
    }
}
