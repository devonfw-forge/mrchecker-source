package com.capgemini.mrchecker.test.core.cucumber;


import com.capgemini.mrchecker.test.core.TestExecutionObserver;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestStepFinished;

import java.util.Optional;

public class EventListenerPlugin implements ConcurrentEventListener {
    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestCaseFinished.class, testCaseFinished);
        eventPublisher.registerHandlerFor(TestStepFinished.class, testStepFinished);
    }

    private final EventHandler<TestCaseFinished> testCaseFinished = event -> {
        switch (event.getResult().getStatus()) {
            case SKIPPED:
                TestExecutionObserver.getInstance().testAborted(null, event.getResult().getError());
                break;
            case PASSED:
                TestExecutionObserver.getInstance().testSuccessful(null);
                break;
            case FAILED:
                TestExecutionObserver.getInstance().testFailed(null, event.getResult().getError());
                break;
            case UNUSED:
            case PENDING:
            case AMBIGUOUS:
            case UNDEFINED:
            default:
                String reason = event.getResult().getStatus().name();
                TestExecutionObserver.getInstance().testDisabled(null, Optional.of(reason));
                break;
        }
        TestExecutionObserver.getInstance().afterAll(null);
    };

    private final EventHandler<TestStepFinished> testStepFinished = event -> {
        if (event.getResult().getError() != null) {
            TestExecutionObserver.getInstance().handleTestExecutionException(null, event.getResult().getError(), false);
        }
    };
}