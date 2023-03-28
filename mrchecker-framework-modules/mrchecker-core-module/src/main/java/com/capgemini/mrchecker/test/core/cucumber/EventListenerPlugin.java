package com.capgemini.mrchecker.test.core.cucumber;


import com.capgemini.mrchecker.test.core.TestExecutionObserver;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseFinished;

public class EventListenerPlugin implements ConcurrentEventListener {
    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestCaseFinished.class, teardown);
    }

    private final EventHandler<TestCaseFinished> teardown = event -> {
        TestExecutionObserver.getInstance().afterAll(null);
    };
}