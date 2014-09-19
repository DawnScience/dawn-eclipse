/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.hdf5.api;

import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.IndexIterator;
import org.eclipse.dawnsci.analysis.dataset.impl.StringDataset;

/**
 * Leaf node to hold a (lazy) dataset or string
 */
public class HDF5Dataset extends HDF5Node {
	private boolean string = false;
	private boolean supported = false; // inhomogeneous composite datasets are not currently supported

	private ILazyDataset dataset;
	private long[] maxShape;
	private String text;
	private String type;

	/**
	 * Construct a HDF5 dataset with given object ID
	 * @param oid object ID
	 */
	public HDF5Dataset(final long oid) {
		super(oid);
	}

	/**
	 * This can return null for empty datasets
	 * @return lazy dataset
	 */
	public ILazyDataset getDataset() {
		return dataset;
	}

	/**
	 * Set given (lazy) dataset
	 * @param lazyDataset
	 */
	public void setDataset(final ILazyDataset lazyDataset) {
		dataset = lazyDataset;
		supported = true;
		string = lazyDataset instanceof StringDataset || lazyDataset.elementClass() == String.class;
	}

	/**
	 * Set string
	 * @param text
	 */
	public void setString(final String text) {
		this.text = text;
		string = true;
		supported = true;
	}

	/**
	 * @return true if dataset is a string or string dataset
	 */
	public boolean isString() {
		return string;
	}

	/**
	 * @return true if this dataset is supported
	 */
	public boolean isSupported() {
		return supported;
	}

	/**
	 * Set dataset to be empty
	 */
	public void setEmpty() {
		dataset = null;
		supported = true;
	}

	/**
	 * Get a string if this dataset is a string or dataset
	 * @return string or null
	 */
	public String getString() {
		if (!string)
			return null;
		if (text != null)
			return text;

		StringDataset a;
		if (dataset instanceof StringDataset)
			a = (StringDataset) dataset;
		else
			a = (StringDataset) dataset.getSlice();

		StringBuilder out = new StringBuilder();
		IndexIterator it = a.getIterator();
		while (it.hasNext()) {
			out.append(a.getAbs(it.index));
			out.append('\n');
		}
		int end = out.length();
		out.delete(end-1, end);
		return out.toString();
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder(super.toString());

		out.append(INDENT);
		if (string) {
			out.append(getString());
		} else if (supported) {
			out.append(dataset == null ? "empty" : dataset.toString());
		} else {
			out.append("unsupported");
		}
		return out.toString();
	}

	/**
	 * Set HDF5 type name
	 * @param name
	 */
	public void setTypeName(String name) {
		type = name;
	}

	/**
	 * @return HDF5 type name
	 */
	public String getTypeName() {
		return type;
	}

	/**
	 * Set maximum shape of dataset
	 * @param maxShape
	 */
	public void setMaxShape(long[] maxShape) {
		this.maxShape = maxShape;
	}

	/**
	 * @return maximum shape
	 */
	public long[] getMaxShape() {
		return maxShape;
	}

}
