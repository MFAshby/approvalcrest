/*
 * Copyright 2013 Shazam Entertainment Limited
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
package com.github.karsaig.approvalcrest;

import static com.github.karsaig.approvalcrest.matchers.ChildBeanMatchers.childStringEqualTo;
import static com.github.karsaig.approvalcrest.model.ChildBean.Builder.child;
import static com.github.karsaig.approvalcrest.model.ParentBean.Builder.parent;
import static com.github.karsaig.approvalcrest.util.AssertionHelper.assertThat;
import static com.github.karsaig.approvalcrest.util.AssertionHelper.sameBeanAs;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.fail;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import com.github.karsaig.approvalcrest.model.ParentBean;

/**
 * Tests which verify the diagnostic displayed when a custom matcher fails.
 */
public class MatcherAssertCustomMatchingDiagnosticTest {

	@Test
	public void includesDescriptionAndMismatchDescriptionForFailingMatcherOnPrimiteField() {
		ParentBean.Builder expected = parent().childBean(child().childString("apple"));
		ParentBean.Builder actual = parent().childBean(child().childString("banana"));
		
		try {
			assertThat(actual, sameBeanAs(expected).with("childBean.childString", equalTo("kiwi")));
			fail("Expected assertion error");
		} catch (AssertionError e) {
			MatcherAssert.assertThat(e.getMessage(), endsWith("and childBean.childString \"kiwi\"\n     but: childBean.childString was \"banana\""));
		}
	}
	
	@Test
	public void includesJsonSnippetOfNonPrimitiveFieldOnMatchFailure() {
		ParentBean.Builder expected = parent().childBean(child().childString("apple"));
		ParentBean.Builder actual = parent().childBean(child().childString("banana").childInteger(1));
		
		try {
			assertThat(actual, sameBeanAs(expected).with("childBean", childStringEqualTo("kiwi")));
			fail("Expected assertion error");
		} catch (AssertionError e) {
			MatcherAssert.assertThat(e.getMessage(), endsWith("and childBean having string field \"kiwi\"\n     but: childBean string field was \"banana\"\n{\n  \"childString\": \"banana\",\n  \"childInteger\": 1\n}"));
		}
	}
	
	@Test
	public void doesNotIncludeJsonSnippetOnNullField() {
		ParentBean.Builder expected = parent().childBean(child().childString("apple"));
		ParentBean.Builder actual = parent();
		
		try {
			assertThat(actual, sameBeanAs(expected).with("childBean", childStringEqualTo("kiwi")));
			fail("Expected assertion error");
		} catch (AssertionError e) {
			MatcherAssert.assertThat(e.getMessage(), endsWith("and childBean having string field \"kiwi\"\n     but: childBean was null"));
		}
	}
	
	@Test
	public void throwsIllegalArgumentExceptionWhenFieldPathDoesNotExist() {
		ParentBean.Builder expected = parent().childBean(child().childString("apple"));
		ParentBean.Builder actual = parent().childBean(child().childString("banana"));
		
		try {
			assertThat(actual, sameBeanAs(expected).with("childBean.nonExistingField", equalTo("kiwi")));
			fail("Expected assertion error");
		} catch (IllegalArgumentException e) {
			MatcherAssert.assertThat(e.getMessage(), endsWith("childBean.nonExistingField does not exist"));
		}
	}
}
