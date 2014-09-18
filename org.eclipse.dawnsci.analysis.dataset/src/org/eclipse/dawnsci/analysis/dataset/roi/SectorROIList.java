/*
 * Copyright 2011 Diamond Light Source Ltd.
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

package org.eclipse.dawnsci.analysis.dataset.roi;

import java.util.ArrayList;

import org.eclipse.dawnsci.analysis.api.roi.IROI;


/**
 * Wrapper for a list of sector ROIs
 */
public class SectorROIList extends ArrayList<SectorROI> implements ROIList<SectorROI> {

	@Override
	public boolean add(IROI roi) {
		if (roi instanceof SectorROI)
			return super.add((SectorROI) roi);
		return false;
	}

	@Override
	public boolean append(SectorROI roi) {
		return super.add(roi);
	}
}
