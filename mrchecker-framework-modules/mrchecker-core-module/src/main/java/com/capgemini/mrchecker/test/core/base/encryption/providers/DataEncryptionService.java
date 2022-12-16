package com.capgemini.mrchecker.test.core.base.encryption.providers;

import com.capgemini.mrchecker.test.core.base.encryption.IDataEncryptionService;
import com.capgemini.mrchecker.test.core.exceptions.BFSecureModuleException;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

/**
 * Basic data encryption service to encapsulate and isolate the encryption/decryption logic.
 *
 * @author Lukasz Stefaniszyn, Capgemini
 * @author Marek Puchalski, Capgemini
 */
public class DataEncryptionService implements IDataEncryptionService {

    public static final int SECRET_MIN_LENGTH = 8;
    private static IDataEncryptionService instance;
    private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    private DataEncryptionService(InputStream secretSource) {
        setSecret(readSecret(secretSource));
    }

    /**
     * Initializes the service based on the path to a file containing the secret.
     * NOTE: This is the best way to instantiate the service, as you should be holding
     * the secret in a file that is not checked into your repository, and is passed
     * among your team members over a different channel.
     *
     * @param secretSource A stream input for a secret
     */

    public static void init(InputStream secretSource) {
        if (Objects.isNull(instance)) {
            synchronized (DataEncryptionService.class) {
                if (Objects.isNull(instance)) {
                    instance = new DataEncryptionService(secretSource);
                }
            }
        }
    }

    public static IDataEncryptionService getInstance() {
        return DataEncryptionService.instance;
    }

    public static void delInstance() {
        DataEncryptionService.instance = null;
    }

    private String readSecret(InputStream secretSource) {
        try (Scanner scanner = new Scanner(secretSource)) {
            return readLine(scanner, secretSource).trim();
        }
    }

    private String readLine(Scanner scanner, InputStream secretSource) {
        if (scanner.hasNext()) {
            return scanner.next();
        } else {
            throw new BFSecureModuleException("Secret source is empty: " + secretSource);
        }
    }

    @Override
    public String encrypt(String text) {
        return PropertyValueEncryptionUtils.encrypt(text, encryptor);
    }

    @Override
    public String decrypt(String text) {
        if (!isEncrypted(text)) {
            throw new BFSecureModuleException("Text is not encrypted: " + text);
        }

        return PropertyValueEncryptionUtils.decrypt(text, encryptor);
    }

    @Override
    public void setSecret(String secret) {
        if (Objects.isNull(secret)) {
            throw new BFSecureModuleException("Secret must not be NULL");
        }

        if (!secret.trim()
                .equals(secret)) {
            throw new BFSecureModuleException("Secret contains whitespaces which are trimable. "
                    + "This can cause problems when using command line tools for encryption, due which is not allowed.");
        }

        if (secret.length() < SECRET_MIN_LENGTH) {
            throw new BFSecureModuleException("Secrets must not be shorter than " + SECRET_MIN_LENGTH + " characters");
        }

        if (encryptor.isInitialized()) {
            encryptor = new StandardPBEStringEncryptor();
        }

        encryptor.setPassword(secret);
    }

    @Override
    public boolean isEncrypted(String text) {
        if (Objects.isNull(text)) {
            return false;
        }

        return PropertyValueEncryptionUtils.isEncryptedValue(text);
    }
}
