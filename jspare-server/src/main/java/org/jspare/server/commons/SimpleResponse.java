/*
 * Copyright 2016 JSpare.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jspare.server.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Instantiates a new simple response.
 *
 * @param result
 *            the result
 * @param message
 *            the message
 * @param reasons
 *            the reasons
 */
@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class SimpleResponse {

	/**
	 * Invalid constraints.
	 *
	 * @param violedConstraints
	 *            the violed constraints
	 * @return the simple response
	 */
	public static SimpleResponse invalidConstraints(Set<ConstraintViolation<Object>> violedConstraints) {

		SimpleResponse simpleError = new SimpleResponse().result("ERROR").message("Invalid constraints");
		violedConstraints.forEach(v -> simpleError.reason(new Reason(v.getPropertyPath().toString(), v.getMessage())));
		return simpleError;
	}

	/**
	 * Ok.
	 *
	 * @return the simple response
	 */
	public static SimpleResponse ok() {

		return new SimpleResponse();
	}

	/** The result. */
	private String result = "OK";

	/** The message. */
	private String message;

	/** The reasons. */
	private List<Reason> reasons;

	/**
	 * Gets the reasons.
	 *
	 * @return the reasons
	 */
	public List<Reason> getReasons() {

		if (reasons == null) {
			reasons = new ArrayList<>();
		}
		return reasons;
	}

	/**
	 * Reason.
	 *
	 * @param reason
	 *            the reason
	 * @return the simple response
	 */
	public SimpleResponse reason(Reason reason) {

		getReasons().add(reason);
		return this;
	}
}