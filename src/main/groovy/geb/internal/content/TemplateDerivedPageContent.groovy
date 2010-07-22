/* Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geb.internal.content

import geb.Page
import geb.error.RequiredPageContentNotPresent
import be.roam.hue.doj.Doj

abstract class TemplateDerivedPageContent implements PageContent {

	private PageContentTemplate _template
	private Object[] _args
	@Delegate private Doj _navigator
	
	/**
	 * Called by the template when created (i.e. is not public).
	 * 
	 * We don't use a constructor to prevent users from having to implement them.
	 */
	void init(PageContentTemplate template, Doj navigator, Object[] args) {
		this._template = template
		this._navigator = navigator
		this._args = args
	}
	
	String toString() {
		"$_template.name - ${this.class.simpleName} (owner: $_template.owner, args: $_args)"
	}
	
	Doj getNavigator() {
		_navigator
	}
	
	Page getPage() {
		_template.page
	}
	
	boolean isPresent() {
		!_navigator.empty
	}
	
	void require() {
		if (!isPresent()) {
			throw new RequiredPageContentNotPresent(this)
		}
		this
	}
	
	void click() {
		click(_template.toPage)
	}
	
	void click(Class pageClass) {
		_navigator.click()
		if (pageClass) {
			_template.page.driver.page(pageClass)
		}
	}
	
	/**
	 * @see get(String)
	 */
	def find(int index) {
		navigator.get(index)
	}

	/**
	 * @see get(String)
	 */	
	def find(String name) {
		navigator.get(name)
	}
	
	/**
	 * @see get(String)
	 */
	def find(String selector, int index) {
		navigator.get(selector, index)
	}
	
	/**
	 * Groovy will delegate property access to a get(String) method,
	 * so we need to override to get defined content. For selecting content,
	 * we have to use find() to start navigating.
	 * 
	 * The doj replacement should not have a get(String) method for this reason.
	 */
	def get(String name) {
		find(name)
	}

}