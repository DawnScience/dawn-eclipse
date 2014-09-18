/*-
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

package org.eclipse.dawnsci.analysis.dataset.roi.handler;

import org.eclipse.dawnsci.analysis.dataset.roi.LinearROI;

/**
 * Wrapper class for a LinearROI that adds handles
 */
public class LinearROIHandler extends ROIHandler<LinearROI> {

	/**
	 * Number of handle areas
	 */
	private final static int NHANDLE = 3;
	
	/**
	 * Handler for LinearROI
	 * @param roi
	 */
	public LinearROIHandler(LinearROI roi) {
		super();
		for (int h = 0; h < NHANDLE; h++) {
			add(-1);
		}
		this.roi = roi;
	}

	@Override
	public int getCentreHandle() {
		return 1;
	}

	@Override
	public double[] getHandlePoint(int handle, int size) {
		double[] pt = getAnchorPoint(handle, size);
		
		if (pt != null) {
			pt[0] -= size/2;
			pt[1] -= size/2;
		}
		return pt;
	}

	@Override
	public double[] getAnchorPoint(int handle, int size) {
		double[] pt = null;

		switch (handle) {
		case 0:
			pt = roi.getPoint();
			break;
		case 1:
			pt = roi.getMidPoint();
			break;
		case 2:
			pt = roi.getEndPoint();
			break;
		}

		return pt;
	}

	/**
	 * @param pt
	 * @return reoriented ROI
	 */
	public LinearROI reorient(double[] pt) {
		LinearROI croi = null;
		double len;

		switch (handle) {
		case 0:
			croi = roi.copy();
			len = croi.getLength();
			croi.setPointKeepEndPoint(pt);
			croi.translateAlongLength(croi.getLength()-len);
			croi.setLength(len);
			break;
		case 2:
			croi = roi.copy();
			len = croi.getLength();
			croi.setEndPoint(pt);
			croi.setLength(len);
			break;
		}
		return croi;
	}

	/**
	 * @param pt
	 * @return resized ROI
	 */
	public LinearROI resize(double[] pt) {
		LinearROI croi = null;

		switch (handle) {
		case 0:
			croi = roi.copy();
			croi.setPointKeepEndPoint(pt);
			break;
		case 2:
			croi = roi.copy();
			croi.setEndPoint(pt);
			break;
		}
		return croi;
	}

	@Override
	public LinearROI interpretMouseDragging(double[] spt, double[] ept) {
		LinearROI croi = null; // return null if not a valid event

		switch (status) {
		case RMOVE:
			croi = roi.copy();
			croi.addPoint(ept);
			croi.subPoint(spt);
			break;
		case NONE:
			croi = roi.copy();
			croi.setEndPoint(ept);
			break;
		case REORIENT:
			croi = reorient(ept);
			break;
		case RESIZE:
			croi = resize(ept);
			break;
		case ROTATE:
			croi = roi.copy();
			double ang = croi.getAngleRelativeToMidPoint(ept);
			double[] mpt = croi.getMidPoint();
			croi.setAngle(ang);
			croi.setMidPoint(mpt);
			break;
		case CMOVE:
			break;
		case CRMOVE:
			break;
		}

		return croi;
	}
}
