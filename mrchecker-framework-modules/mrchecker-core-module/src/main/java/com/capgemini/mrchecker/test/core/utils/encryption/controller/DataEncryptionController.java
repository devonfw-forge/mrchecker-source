package com.capgemini.mrchecker.test.core.utils.encryption.controller;

import com.capgemini.mrchecker.test.core.base.encryption.IDataEncryptionService;
import com.capgemini.mrchecker.test.core.base.encryption.providers.DataEncryptionService;
import com.capgemini.mrchecker.test.core.exceptions.BFSecureModuleException;
import com.capgemini.mrchecker.test.core.utils.encryption.CryptParams;
import com.capgemini.mrchecker.test.core.utils.encryption.exceptions.EncryptionServiceException;
import com.capgemini.mrchecker.test.core.utils.encryption.view.IDataEncryptionView;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

import java.io.ByteArrayInputStream;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class DataEncryptionController implements IDataEncryptionController {
    private IDataEncryptionView view;
    private final IDataEncryptionService dataEncryptionService;

    public DataEncryptionController() {
        DataEncryptionService.init(new ByteArrayInputStream("defaultSecret".getBytes()));
        dataEncryptionService = DataEncryptionService.getInstance();
    }

    @Override
    public void setView(IDataEncryptionView view) {
        this.view = view;
    }

    @Override
    public void onEncrypt(CryptParams cryptParams) {
        crypt(cryptParams, dataEncryptionService::encrypt, view::setEncryptionFieldValue);
    }

    @Override
    public void onDecrypt(CryptParams cryptParams) {
        crypt(cryptParams, dataEncryptionService::decrypt, view::setDecryptionFieldValue);
    }

    private void crypt(CryptParams cryptParams, UnaryOperator<String> cryptFunction, Consumer<String> resultFieldSetter) {
        try {
            dataEncryptionService.setSecret(cryptParams.getSecret());
            resultFieldSetter.accept(cryptFunction.apply(cryptParams.getText()));
        } catch (BFSecureModuleException e) {
            throw new EncryptionServiceException(e);
        } catch (EncryptionOperationNotPossibleException e) {
            throw new EncryptionServiceException(new EncryptionOperationNotPossibleException("Wrong key for the given cipher"));
        }
    }
}
