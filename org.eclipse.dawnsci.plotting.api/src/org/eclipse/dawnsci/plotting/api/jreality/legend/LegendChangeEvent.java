/*
 * Copyright 2012 Diamond Light Source Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.dawnsci.plotting.api.jreality.legend;

import java.util.EventObject;

/**
 *
 */
public class LegendChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int legendEntryNr;
	
	public LegendChangeEvent(Object source, int entryNr) {
		super(source);
		this.legendEntryNr = entryNr;
	}

	public int getEntryNr() {
		return legendEntryNr;
	}


}