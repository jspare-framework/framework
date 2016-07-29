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
package org.jspare.ui.view;

import static org.jspare.core.commons.Definitions.DEFAULT_CHARSET;
import static org.jspare.core.container.Environment.CONFIG;
import static org.jspare.ui.commons.UiDefinitions.ROOT_FILE;
import static org.jspare.ui.commons.UiDefinitions.ROOT_FILE_KEY;
import static org.jspare.ui.commons.UiDefinitions.SUFIX_FILE_KEY;
import static org.jspare.ui.commons.UiDefinitions.SUFIX_PAGE_FILE;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

import org.jspare.core.container.Inject;
import org.jspare.core.loader.ResourceLoader;
import org.jspare.server.exception.LoadTemplateException;
import org.jspare.server.exception.RenderableException;

import lombok.AllArgsConstructor;

/**
 * The Class UiToolkitImpl.
 *
 * @author pflima
 * @since 22/04/2016
 */
public class UiToolkitImpl implements UiToolkit {

	/**
	 * Instantiates a new simple template.
	 *
	 * @param content
	 *            the content
	 */

	/**
	 * Instantiates a new simple template.
	 *
	 * @param content
	 *            the content
	 */
	@AllArgsConstructor
	private class SimpleTemplate implements Template {

		/** The Constant QUOTE. */
		private static final String QUOTE = "@=";

		/** The content. */
		private String content;

		/*
		 * (non-Javadoc)
		 *
		 * @see org.jspare.ui.view.Template#apply(java.util.Map)
		 */
		@Override
		public String apply(Map<String, Object> viewValues) {
			String result = content;

			for (Entry<String, Object> entry : viewValues.entrySet()) {

				result = result.replaceAll(QUOTE.concat(entry.getKey()), Matcher.quoteReplacement(entry.getValue().toString()));
			}
			return new String(result.getBytes(DEFAULT_CHARSET), DEFAULT_CHARSET);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.jspare.ui.view.Template#setContent(java.lang.Object)
		 */
		@Override
		public void setContent(Object content) {
			this.content = (String) content;
		}

	}

	/** The templates. */
	private Map<String, SimpleTemplate> templates = new ConcurrentHashMap<>();

	/** The resource loader. */
	@Inject
	private ResourceLoader resourceLoader;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.jspare.ui.view.UiToolkit#compile(org.jspare.ui.view.Template,
	 * java.util.Map)
	 */
	@Override
	public String compile(Template template, Map<String, Object> viewValues) throws RenderableException {

		return template.apply(viewValues);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.jspare.ui.view.UiToolkit#convert2Template(java.lang.String)
	 */
	@Override
	public Template convert2Template(String content) throws LoadTemplateException {

		return new SimpleTemplate(content);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.jspare.ui.view.UiToolkit#loadTemplate(java.lang.String)
	 */
	@Override
	public Template loadTemplate(String resource) throws LoadTemplateException {

		try {

			return loadTempateIfNecessary(resource);
		} catch (IOException e) {

			throw new LoadTemplateException(e);
		}
	}

	/**
	 * Load tempate if necessary.
	 *
	 * @param resource
	 *            the resource
	 * @return the template
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private Template loadTempateIfNecessary(String resource) throws IOException {

		if (!resource.endsWith(CONFIG.get(SUFIX_FILE_KEY, SUFIX_PAGE_FILE))) {

			resource += CONFIG.get(SUFIX_FILE_KEY, SUFIX_PAGE_FILE);
		}

		resource = CONFIG.get(ROOT_FILE_KEY, ROOT_FILE) + resource;

		// TODO CACHE TEMPLATES
		boolean loadTemplates = false;
		if (!loadTemplates) {

			return new SimpleTemplate(resourceLoader.readFileToString(resource));
		}

		if (!templates.containsKey(resource)) {
			String template = resourceLoader.readFileToString(resource);
			templates.put(resource, new SimpleTemplate(template));
		}

		return templates.get(resource);
	}
}
