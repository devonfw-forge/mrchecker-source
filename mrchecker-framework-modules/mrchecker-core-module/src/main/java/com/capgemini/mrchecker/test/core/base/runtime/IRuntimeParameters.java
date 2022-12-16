package com.capgemini.mrchecker.test.core.base.runtime;

/**
 * @author LUSTEFAN
 */
public interface IRuntimeParameters {

    /**
     * @return value of parameter
     */
    String getValue();

    /**
     * @return parameter key (name)
     */
    String getKey();

    /**
     * Read one more time Runtime parameters
     */
    void refreshParameterValue();

}
