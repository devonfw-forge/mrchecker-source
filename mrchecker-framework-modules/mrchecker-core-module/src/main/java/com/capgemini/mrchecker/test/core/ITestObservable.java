package com.capgemini.mrchecker.test.core;

public interface ITestObservable {
    void addObserver(ITestObserver testObserver);

    void removeObserver(ITestObserver testObserver);
}
