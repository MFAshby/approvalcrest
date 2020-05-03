/*
 * Copyright 2013 Shazam Entertainment Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.github.karsaig.approvalcrest.matcher;

import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.TestInfo;

import com.github.karsaig.approvalcrest.Junit5InfoBasedTestMeta;
import com.github.karsaig.approvalcrest.JunitJupiterTestMeta;

import com.google.common.annotations.Beta;

/**
 * Entry point for the matchers available in Approvalcrest.
 */
public class Matchers {

    /**
     * Returns a {@link NullMatcher} in case the expectation is null, a
     * {@link IsEqualMatcher} if it's a primitive, String or Enum or a
     * {@link DiagnosingCustomisableMatcher} otherwise.
     *
     * @param expected the expected bean to match against
     * @param <T>      type of actual object
     * @return an {@link CustomisableMatcher} instance
     */
    public static <T> CustomisableMatcher<T, ?> sameBeanAs(T expected) {
        if (expected == null) {
            return new NullMatcher<>(expected);
        }

        if (isPrimitiveOrWrapper(expected.getClass()) || expected.getClass() == String.class
                || expected.getClass().isEnum()) {
            return new IsEqualMatcher<>(expected);
        }

        return new DiagnosingCustomisableMatcher<>(expected);
    }

    /**
     * Returns a {@link JsonMatcher} for matching an object with a generated
     * file.
     *
     * @param <T> Type of object to serialize to JSON
     * @return a new {@link JsonMatcher} instance
     */
    public static <T> JsonMatcher<T> sameJsonAsApproved() {
        return sameJsonAsApproved(new JunitJupiterTestMeta());
    }

    /**
     * Returns a {@link JsonMatcher} for matching an object with a generated
     * file.
     * Should be used for cases when the default implementation of {@link TestMetaInformation} doesn't work for any reason.
     * <p>
     * <b>!! Beta, as such subject to change !!</b>
     *
     * @param testMetaInformation Information used to generate file names and path to use.
     * @param <T>                 Type of object to serialize to JSON
     * @return a new {@link JsonMatcher} instance
     */
    @Beta
    public static <T> JsonMatcher<T> sameJsonAsApproved(TestMetaInformation testMetaInformation) {
        return new JsonMatcher<>(testMetaInformation);
    }

    /**
     * Returns a {@link JsonMatcher} for matching an object with a generated
     * file.
     * Should be used for cases when the default implementation of {@link TestMetaInformation} doesn't work for any reason.
     *
     * @param testInfo JUnit5 provided test information. {@link TestInfo}.
     * @param <T>      Type of object to serialize to JSON
     * @return a new {@link JsonMatcher} instance
     */
    public static <T> JsonMatcher<T> sameJsonAsApproved(TestInfo testInfo) {
        return getUniqueIndex(testInfo)
                .map(s -> new JsonMatcher<T>(new Junit5InfoBasedTestMeta(testInfo)).withUniqueId(s))
                .orElse(new JsonMatcher<T>(new Junit5InfoBasedTestMeta(testInfo)));
    }

    /**
     * Returns a {@link ContentMatcher} for matching a string with a generated file.
     *
     * @param <T> Only {@link String} is supported at the moment.
     * @return a new {@link ContentMatcher} instance
     */
    public static <T> ContentMatcher<T> sameContentAsApproved() {
        return sameContentAsApproved(new JunitJupiterTestMeta());
    }

    /**
     * Returns a {@link ContentMatcher} for matching a string with a generated file.
     * Should be used for cases when the default implementation of {@link TestMetaInformation} doesn't work for any reason.
     * <p>
     * <b>!! Beta, as such subject to change !!</b>
     *
     * @param testMetaInformation Information used to generate file names and path to use.
     * @param <T>                 Only {@link String} is supported at the moment.
     * @return a new {@link ContentMatcher} instance
     */
    @Beta
    public static <T> ContentMatcher<T> sameContentAsApproved(TestMetaInformation testMetaInformation) {
        return new ContentMatcher<>(testMetaInformation);
    }

    /**
     * Returns a {@link ContentMatcher} for matching a string with a generated file.
     * Should be used for cases when the default implementation of {@link TestMetaInformation} doesn't work for any reason.
     *
     * @param testInfo JUnit5 provided test information. {@link TestInfo}.
     * @param <T>      Only {@link String} is supported at the moment.
     * @return a new {@link ContentMatcher} instance
     */
    public static <T> ContentMatcher<T> sameContentAsApproved(TestInfo testInfo) {
        return getUniqueIndex(testInfo)
                .map(s -> new ContentMatcher<T>(new Junit5InfoBasedTestMeta(testInfo)).withUniqueId(s))
                .orElse(new ContentMatcher<T>(new Junit5InfoBasedTestMeta(testInfo)));
    }

    private static final Pattern TEST_INDEX_MATCHER = Pattern.compile("^\\[(\\d+)\\].*");

    private static Optional<String> getUniqueIndex(TestInfo testInfo) {
        Matcher m = TEST_INDEX_MATCHER.matcher(testInfo.getDisplayName());
        if (m.matches()) {
            return Optional.of(m.group(1));
        }
        return Optional.empty();
    }
}
