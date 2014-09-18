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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.roi.IROI;
import org.eclipse.dawnsci.analysis.dataset.roi.ROIUtils;

/**
 * Abstract class for region of interest handles
 * 
 * Its super class holds the primitive IDs for handle areas
 */
abstract public class ROIHandler<T extends IROI> {
	protected T roi;
	protected int handle;
	protected HandleStatus status;
	protected List<Integer> list;

	public ROIHandler() {
		list = new ArrayList<Integer>();
	}

	public boolean add(int e) {
		return list.add(e);
	}

	public int get(int index) {
		return list.get(index);
	}

	public int set(int index, int element) {
		return list.set(index, element);
	}

	public int remove(int index) {
		return list.remove(index);
	}

	public int size() {
		return list.size();
	}

	public int indexOf(int o) {
		return list.indexOf(o);
	}

	public boolean contains(int o) {
		return list.contains(o);
	}

	public List<Integer> getAll() {
		return list;
	}

	/**
	 * @param handle
	 * @param size 
	 * @return handle point for corner of handle box
	 */
	abstract public double[] getHandlePoint(int handle, int size);

	/**
	 * @param handle
	 * @param size
	 * @return anchor point for scale invariant display and acts as centre of rotation
	 */
	abstract public double[] getAnchorPoint(int handle, int size);

	/**
	 * @return ROI
	 */
	public T getROI() {
		return roi;
	}

	/**
	 * @return centre handle ID
	 */
	abstract public int getCentreHandle();

	/**
	 * @param roi The roi to set.
	 */
	public void setROI(T roi) {
		this.roi = roi;
	}

	/**
	 * Set handle used and status in dragging
	 * @param handle
	 * @param dragStatus
	 */
	public void configureDragging(int handle, HandleStatus dragStatus) {
		this.handle = handle;
		status = dragStatus;
	}

	/**
	 * Reset configuration for dragging
	 */
	public void unconfigureDragging() {
		handle = -1;
		status = HandleStatus.NONE;
	}

	/**
	 * @return handle status
	 */
	public HandleStatus getStatus() {
		return status;
	}

	/**
	 * Interpret mouse dragging
	 * @param spt
	 * @param ept
	 * @return roi
	 */
	public T interpretMouseDragging(int[] spt, int[] ept) {
		return interpretMouseDragging(ROIUtils.convertToDoubleArray(spt), ROIUtils.convertToDoubleArray(ept));
	}

	/**
	 * Interpret mouse dragging
	 * @param spt
	 * @param ept
	 * @return roi
	 */
	abstract public T interpretMouseDragging(double[] spt, double[] ept);
}
