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
package org.jspare.server.controller;

import org.jspare.core.container.Component;
import org.jspare.server.Request;
import org.jspare.server.Response;

/**
 * A factory for creating Controller objects.
 */
@Component
public interface ControllerFactory {

	/**
	 * Dispatch.
	 *
	 * @param cmdClazz
	 *            the cmd clazz
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 */
	void dispatch(Class<?> cmdClazz, Request request, Response response);

	/**
	 * Instantiate.
	 *
	 * @param cmdClazz
	 *            the cmd clazz
	 * @return the object
	 */
	Object instantiate(Class<?> cmdClazz);
}