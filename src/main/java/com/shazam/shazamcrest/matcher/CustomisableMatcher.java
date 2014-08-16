/*
 * Copyright 2013 Shazam Entertainment Limited
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.shazam.shazamcrest.matcher;

import org.hamcrest.Matcher;

/**
 * {@link Matcher} implementation where fields and object types can be skipped from the comparison, or matched with
 * custom matchers.
 */
public interface CustomisableMatcher<T> extends Matcher<T> {
	/**
	 * Specify the path of the field to be skipped from the matcher comparison.
	 * Example:
	 * <pre>sameBeanAs(expected).ignoring("beanField.subBeanField")</pre>
	 * 
	 * @param fieldPath the path of the field to be skipped from the comparison.
	 * @return the instance of the matcher
	 */
	CustomisableMatcher<T> ignoring(String fieldPath);

	/**
	 * Specify the object type of the fields to be skipped from the matcher comparison.
	 * Example:
	 * <pre>sameBeanAs(expected).ignoring(Bean.class)</pre>
	 * 
	 * @param clazz the object type to be skipped from the comparison.
	 * @return the instance of the matcher
	 */
	CustomisableMatcher<T> ignoring(Class<?> clazz);

    /**
     * Specify the object type of the fields to be handled for circular references.
     * Example:
     * <pre>sameBeanAs(expected).circularReference(Bean.class)</pre>
     *
     * @param clazz the object type to be handled by GraphAdapterBuilder.
     * @return the instance of the matcher
     */
    CustomisableMatcher<T> circularReference(Class<?> clazz);

    /**
	 * Specify the path of the field to be matched with a specific matcher.
	 * Example:
	 * <pre>sameBeanAs(expected).with("beanField.subBeanField", contains("element"))</pre>
	 * 
	 * @param fieldPath the path of the field to be matched with the provided matcher.
	 * @param matcher the Hamcrest matcher used to match the specified field.
	 * @return the instance of the matcher
	 */
	<V> CustomisableMatcher<T> with(String fieldPath, Matcher<V> matcher);
}