package com.capgemini.mrchecker.test.core.utils.encryption.view;

import com.capgemini.mrchecker.test.core.utils.encryption.CryptParams;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.IDataEncryptionController;
import com.capgemini.mrchecker.test.core.utils.encryption.exceptions.EncryptionServiceException;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.function.Supplier;

import static javax.swing.JOptionPane.showMessageDialog;

public class DataEncryptionGUI extends JFrame implements IDataEncryptionView {

    private JPanel contentPane;
    private JTabbedPane mainPane;
    private JPanel encryptionPane;
    private JPanel decryptionPane;
    private JTextField encryptionTextField;
    private JTextField encryptionKeyTextField;
    private JButton chooseEncryptionKeyButton;
    private JButton encryptButton;
    private JLabel encryptionKeyLabel;
    private JLabel encryptionLabel;
    private JLabel encryptionResultLabel;
    private JTextField encryptionResultTextField;
    private JTextField decryptionTextField;
    private JTextField decryptionResultTextField;
    private JButton decryptButton;
    private JButton chooseDecryptionKeyButton;
    private JTextField decryptionKeyTextField;
    private JLabel decryptionKeyLabel;
    private JLabel decryptionLabel;
    private JLabel decryptionResultLabel;

    private Supplier<String> stringSupplier = () -> null;

    public DataEncryptionGUI(IDataEncryptionController dataEncryptionController) {
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setTitle("Data Encryption Service App");

        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/mrCheckerLogo.png"))).getImage());

        encryptionResultTextField.setEditable(false);
        decryptionResultTextField.setEditable(false);

        chooseEncryptionKeyButton.addActionListener(e -> setKeyFieldValue(encryptionKeyTextField));
        chooseDecryptionKeyButton.addActionListener(e -> setKeyFieldValue(decryptionKeyTextField));

        encryptButton.addActionListener(e -> {
            try {
                dataEncryptionController.onEncrypt(new CryptParams(encryptionKeyTextField.getText(), encryptionTextField.getText()));
            } catch (EncryptionServiceException ex) {
                setEncryptionFieldValue("");
                showMessageDialog(contentPane, "Could not encrypt because:\n" + ex.getMessage());
            }
        });

        decryptButton.addActionListener(e -> {
            try {
                dataEncryptionController.onDecrypt(new CryptParams(decryptionKeyTextField.getText(), decryptionTextField.getText()));
            } catch (EncryptionServiceException ex) {
                setDecryptionFieldValue("");
                showMessageDialog(contentPane, "Could not decrypt because:\n" + ex.getMessage());
            }
        });

        setVisible(true);
    }

    public void setKeyFieldValue(JTextField keyTextField) {
        if (Objects.isNull(stringSupplier.get())) {
            setStringSupplierValue();
        }
        keyTextField.setText(stringSupplier.get());
        stringSupplier = () -> null;
    }

    private void setStringSupplierValue() {
        stringSupplier = () -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    return Files.readAllLines(selectedFile.toPath())
                            .stream()
                            .findFirst()
                            .orElseThrow(() -> new IOException("Empty file"));
                } catch (IOException e) {
                    showMessageDialog(contentPane, "Could not read key from given file\nError: " + e.getMessage());
                    return "";
                }
            } else {
                showMessageDialog(contentPane, "You need to choose a file or provide the key");
                return "";
            }
        };
    }

    @Override
    public void setStringSupplier(String stringSupplier) {
        this.stringSupplier = () -> stringSupplier;
    }

    @Override
    public void setEncryptionFieldValue(String text) {
        encryptionResultTextField.setText(text);
    }

    @Override
    public void setDecryptionFieldValue(String text) {
        decryptionResultTextField.setText(text);
    }

    {
        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your
     * code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        mainPane = new JTabbedPane();
        contentPane.add(mainPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        encryptionPane = new JPanel();
        encryptionPane.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        encryptionPane.setName("encryptionPane");
        mainPane.addTab("Encryption", encryptionPane);
        encryptionKeyLabel = new JLabel();
        encryptionKeyLabel.setText("Key:");
        encryptionPane.add(encryptionKeyLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        encryptionLabel = new JLabel();
        encryptionLabel.setText("Value:");
        encryptionPane.add(encryptionLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        encryptionTextField = new JTextField();
        encryptionTextField.setName("encryptionTextField");
        encryptionTextField.setText("");
        encryptionTextField.setToolTipText("Provide text to encryption...");
        encryptionPane.add(encryptionTextField, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        encryptionKeyTextField = new JTextField();
        encryptionKeyTextField.setName("encryptionKeyTextField");
        encryptionKeyTextField.setText("");
        encryptionKeyTextField.setToolTipText("Provide text or choose key from file...");
        encryptionPane.add(encryptionKeyTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        chooseEncryptionKeyButton = new JButton();
        chooseEncryptionKeyButton.setName("chooseEncryptionKeyButton");
        chooseEncryptionKeyButton.setText("Choose File");
        encryptionPane.add(chooseEncryptionKeyButton, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        encryptionResultTextField = new JTextField();
        encryptionResultTextField.setName("encryptionResultTextField");
        encryptionResultTextField.setText("");
        encryptionResultTextField.setToolTipText("Result of encryption...");
        encryptionPane.add(encryptionResultTextField, new GridConstraints(3, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        encryptionResultLabel = new JLabel();
        encryptionResultLabel.setText("Encrytpion result:");
        encryptionPane.add(encryptionResultLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        encryptButton = new JButton();
        encryptButton.setName("encryptButton");
        encryptButton.setText("Encrypt");
        encryptionPane.add(encryptButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        decryptionPane = new JPanel();
        decryptionPane.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        decryptionPane.setName("decryptionPane");
        mainPane.addTab("Decryption", decryptionPane);
        decryptionKeyLabel = new JLabel();
        decryptionKeyLabel.setText("Key:");
        decryptionPane.add(decryptionKeyLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        decryptionLabel = new JLabel();
        decryptionLabel.setText("Value:");
        decryptionPane.add(decryptionLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        decryptionTextField = new JTextField();
        decryptionTextField.setName("decryptionTextField");
        decryptionTextField.setToolTipText("Provide text to decryption...");
        decryptionPane.add(decryptionTextField, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        decryptionResultLabel = new JLabel();
        decryptionResultLabel.setText("Decryption result:");
        decryptionPane.add(decryptionResultLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        decryptionResultTextField = new JTextField();
        decryptionResultTextField.setName("decryptionResultTextField");
        decryptionResultTextField.setToolTipText("Result of decryption...");
        decryptionPane.add(decryptionResultTextField, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        decryptButton = new JButton();
        decryptButton.setName("decryptButton");
        decryptButton.setText("Decrypt");
        decryptionPane.add(decryptButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chooseDecryptionKeyButton = new JButton();
        chooseDecryptionKeyButton.setName("chooseDecryptionKeyButton");
        chooseDecryptionKeyButton.setText("Choose File");
        decryptionPane.add(chooseDecryptionKeyButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        decryptionKeyTextField = new JTextField();
        decryptionKeyTextField.setName("decryptionKeyTextField");
        decryptionKeyTextField.setToolTipText("Provide text or choose key from file...");
        decryptionPane.add(decryptionKeyTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
