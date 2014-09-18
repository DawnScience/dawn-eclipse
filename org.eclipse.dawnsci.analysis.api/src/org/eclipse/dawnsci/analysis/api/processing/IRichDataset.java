/*-
 * Copyright 2014 Diamond Light Source Ltd.
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

package org.eclipse.dawnsci.analysis.api.processing;

import java.util.Map;

import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;

/**
 * This dataset holds several bits of information about scientific data.
 * 
 * The class contains methods for getting/setting the data components and
 * operating on the rich dataset.
 */
public interface IRichDataset {

	// TODO much of what rich dataset performed is now handled by the metadatatype classes,
	// should probably remove it
	
	// Boiler plate getters and setters for data
	public ILazyDataset getData();

	public Map<Integer, String> getSlicing();

}