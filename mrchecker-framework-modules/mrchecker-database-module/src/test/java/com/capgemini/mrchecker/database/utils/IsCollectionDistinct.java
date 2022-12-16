package com.capgemini.mrchecker.database.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IsCollectionDistinct<T> extends BaseMatcher<T> {

    private final List<Object> duplicates = new ArrayList<>();

    @Override
    public boolean matches(Object o) {
        if (!(o instanceof Iterable)) {
            throw new IllegalArgumentException("Must be of Iterable type");
        }

        Set<Object> uniqueValues = new HashSet<>();
        ((Iterable<Object>) o).forEach(object -> {
            if (!uniqueValues.add(object)) duplicates.add(object);
        });

        return duplicates.isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("No duplicates but found: " + duplicates.toString());
    }

    @Factory
    public static <T> Matcher<T> isCollectionDistinct() {
        return new IsCollectionDistinct();
    }
}