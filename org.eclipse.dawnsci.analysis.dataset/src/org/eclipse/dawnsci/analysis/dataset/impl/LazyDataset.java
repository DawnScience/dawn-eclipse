/*-
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

package org.eclipse.dawnsci.analysis.dataset.impl;

import java.io.Serializable;
import java.util.Arrays;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.api.io.ILazyLoader;
import org.eclipse.dawnsci.analysis.api.monitor.IMonitor;

/**
 * Class that implements lazy dataset interface
 */
public class LazyDataset extends LazyDatasetBase implements Cloneable, Serializable {

	/**
	 * Update this when there are any serious changes to API
	 */
	protected static final long serialVersionUID = -2729953855285411120L;

	private int[]        oShape; // original shape
	protected long       size;   // number of items
	protected ILazyLoader loader;
	private int          dtype;
	private int          isize; // number of elements per item
	private int          oOffset; // original shape offset (first non-unit dimension)
	private int          nOffset; // current shape offset
	protected LazyDataset base = null;
	private int[] sliceStart = null;
	private int[] sliceStep  = null;

	/**
	 * Create a lazy dataset
	 * @param name
	 * @param dtype dataset type
	 * @param elements
	 * @param shape
	 * @param loader
	 */
	public LazyDataset(String name, int dtype, int elements, int[] shape, ILazyLoader loader) {
		this.name = name;
		this.shape = shape;
		oShape = shape;
		this.loader = loader;
		this.dtype = dtype;
		this.isize = elements;
		try {
			size = AbstractDataset.calcLongSize(shape);
		} catch (IllegalArgumentException e) {
			size = Long.MAX_VALUE; // this indicates that the entire dataset cannot be read in! 
		}
		oOffset = -1;
		nOffset = -1;
		for (int i = 0; i < oShape.length; i++) {
			if (oShape[i] != 1) {
				oOffset = nOffset = i;
				break;
			}
		}
	}

	/**
	 * Create a lazy dataset
	 * @param name
	 * @param dtype dataset type
	 * @param shape
	 * @param loader
	 */
	public LazyDataset(String name, int dtype, int[] shape, ILazyLoader loader) {
		this(name, dtype, 1, shape, loader);
	}

	@Override
	public LazyDataset clone() {
		LazyDataset ret = new LazyDataset(new String(name), dtype, shape.clone(), loader);
		if (sliceStart != null) {
			ret.sliceStart = sliceStart.clone();
			ret.sliceStep = sliceStep.clone();
		}
		ret.metadata = copyMetadata();
		return ret;
	}

	@Override
	public int getElementsPerItem() {
		return isize;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;

		LazyDataset other = (LazyDataset) obj;
		if (dtype != other.dtype) {
			return false;
		}
		if (isize != other.isize) {
			return false;
		}
		if (!Arrays.equals(sliceStart, other.sliceStart)) {
			return false;
		}
		if (!Arrays.equals(sliceStep, other.sliceStep)) {
			return false;
		}
		return true;
	}

	@Override
	public int getSize() {
		return (int) size;
	}

	@Override
	public int getDtype() {
		return dtype;
	}

	@Override
	public void setShape(int... shape) {
		setShapeInternal(shape);
	}

	private void setShapeInternal(int... shape) {
		long nsize = AbstractDataset.calcLongSize(shape);
		if (nsize != size) {
			throw new IllegalArgumentException("Size of new shape is not equal to current size");
		}
		if (nsize == 1) {
			this.shape = shape.clone();
			return;
		}

		int da = -1; // length of first non-one dimension
		if (oOffset >= 0) {
			da = oShape[oOffset];
		}
		assert da >= 0;

		int jstop = -1; // index of last non-one dimension + 1
		for (int i = oShape.length - 1; i >= oOffset; i--) {
			if (oShape[i] != 1) {
				jstop = i + 1;
				break;
			}
		}

		int i = 0;
		for (; i < shape.length; i++) {
			if (shape[i] == da) {
				break;
			}
		}

		int off = i;
		if (shape.length - off < jstop - oOffset) {
			throw new IllegalArgumentException("New shape not allowed - can only increase rank by prepending or postpending ones to old shape");
		}
		int j = oOffset;
		for (; i < shape.length && j < jstop; i++, j++) {
			if (shape[i] != oShape[j]) {
				throw new IllegalArgumentException("New shape not allowed - can only increase rank by prepending or postpending ones to old shape");
			}
		}
		nOffset = off;
		this.shape = shape.clone();
		reshapeMetadata(shape);
	}

	@Override
	public ILazyDataset squeeze() {
		return squeeze(false);
	}

	@Override
	public ILazyDataset squeeze(boolean onlyFromEnd) {
		setShapeInternal(AbstractDataset.squeezeShape(shape, onlyFromEnd));
		return this;
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();

		if (name != null && name.length() > 0) {
			out.append("Lazy dataset '");
			out.append(name);
			out.append("' has shape [");
		} else {
			out.append("Lazy dataset shape is [");
		}
		int rank = shape == null ? 0 : shape.length;

		if (rank > 0 && shape[0] >= 0) {
			out.append(shape[0]);
		}
		for (int i = 1; i < rank; i++) {
			out.append(", " + shape[i]);
		}
		out.append(']');

		return out.toString();
	}

	@Override
	public IDataset getSlice(Slice... slice) {
		try {
			return getSlice(null, slice);
		} catch (Exception e) {
			logger.error("Problem slicing lazy dataset", e);
		}
		return null;
	}

	@Override
	public IDataset getSlice(int[] start, int[] stop, int[] step) {
		try {
			return getSlice(null, start, stop, step);
		} catch (Exception e) {
			logger.error("Problem slicing lazy dataset", e);
		}
		return null;
	}

	@Override
	public IDataset getSlice(IMonitor monitor, Slice... slice) throws Exception {
		final int rank = shape.length;
		final int[] start = new int[rank];
		final int[] stop = new int[rank];
		final int[] step = new int[rank];
		Slice.convertFromSlice(slice, shape, start, stop, step);
		return getSlice(monitor, start, stop, step);
	}

	@Override
	public IDataset getSlice(IMonitor monitor, int[] start, int[] stop, int[] step) throws Exception {
		if (loader != null && !loader.isFileReadable())
			return null; // TODO add interaction to use plot server to load dataset

		int rank = shape.length;
		int[] lstart;
		int[] lstop;
		int[] lstep;
		if (step == null) {
			lstep = new int[rank];
			Arrays.fill(lstep, 1);
		} else {
			lstep = step;
		}

		if (start == null) {
			lstart = new int[rank];
		} else {
			lstart = start;
		}

		if (stop == null) {
			lstop = getShape();
		} else {
			lstop = stop;
		}

		int[] lshape = AbstractDataset.checkSlice(shape, start, stop, lstart, lstop, lstep);
		int[] nstart;
		int[] nstop;
		int[] nstep;
		int[] nshape;

		int r = oShape.length;
		if (r != shape.length || oOffset != nOffset) {
			nstart = new int[r];
			nstop = new int[r];
			nstep = new int[r];
			nshape = new int[r];
			int i = 0;
			for (; i < oOffset; i++) {
				nstart[i] = 0;
				nstop[i]  = 1;
				nstep[i]  = 1;
				nshape[i] = 1;
			}
			int j = nOffset;
			for (; i < r && j < shape.length; i++, j++) {
				nstart[i] = lstart[j];
				nstop[i]  = lstop[j];
				nstep[i]  = lstep[j];
				nshape[i] = lshape[j];
			}
			for (; i < r; i++) {
				nstart[i] = 0;
				nstop[i]  = 1;
				nstep[i]  = 1;
				nshape[i] = 1;
			}
		} else {
			nstart = lstart;
			nstop  = lstop;
			nstep  = lstep;
			nshape = lshape;
		}

		if (base != null) {
			for (int i = 0; i < r; i++) {
				nstart[i] = sliceStart[i] + nstart[i] * sliceStep[i];
				nstop[i]  = sliceStart[i] + (nstop[i] - 1) * sliceStep[i] + 1;
				nstep[i]  = nstep[i] * sliceStep[i];
			}
			return base.getSlice(monitor, nstart, nstop, nstep);
		}

		IDataset a;
		try {
			a = loader.getDataset(monitor, oShape, nstart, nstop, nstep);
			a.setShape(lshape);
		} catch (Exception e) {
			// return a fake dataset to show that this has not worked, should not be used in general though.
			logger.debug("Problem getting {}: {}", String.format("slice %s %s %s", Arrays.toString(start), Arrays.toString(stop),
							Arrays.toString(step)), e);
			a = new DoubleDataset(1);
		}
		a.setName(name + AbstractDataset.BLOCK_OPEN + Slice.createString(oShape, nstart, nstop, nstep) + AbstractDataset.BLOCK_CLOSE);
		if (metadata != null && a instanceof LazyDatasetBase) {
			((LazyDatasetBase) a).metadata = copyMetadata();
			((LazyDatasetBase) a).sliceMetadata(false, lstart, lstop, lstep, shape);
		}

		return a;
	}

	@Override
	public ILazyDataset getSliceView(Slice... slice) {
		final int rank = shape.length;
		if (slice == null || slice.length == 0) {
			return getSliceView((int[]) null, null, null);
		}
		final int[] start = new int[rank];
		final int[] stop = new int[rank];
		final int[] step = new int[rank];
		Slice.convertFromSlice(slice, shape, start, stop, step);
		ILazyDataset sliceView = getSliceView(start, stop, step);
		return sliceView;
	}

	@Override
	public LazyDataset getSliceView(int[] start, int[] stop, int[] step) {
		int[] lstart, lstop, lstep;
		final int rank = shape.length;

		if (step == null) {
			lstep = new int[rank];
			Arrays.fill(lstep, 1);
		} else {
			lstep = step;
		}

		if (start == null) {
			lstart = new int[rank];
		} else {
			lstart = start;
		}

		if (stop == null) {
			lstop = new int[rank];
		} else {
			lstop = stop;
		}

		int[] nShape;
		if (rank > 1 || (rank > 0 && shape[0] > 0)) {
			nShape = AbstractDataset.checkSlice(shape, start, stop, lstart, lstop, lstep);
		} else {
			nShape = new int[rank];
		}
		LazyDataset lazy = new LazyDataset(name + "[" + Slice.createString(nShape, lstart, lstop, lstep) + "]",
				dtype, nShape, null);
		lazy.sliceStart = lstart.clone();
		lazy.sliceStep  = lstep.clone();
		lazy.base = base == null ? this : base;
		lazy.metadata = copyMetadata();
		lazy.sliceMetadata(true, lstart, lstop, lstep, shape);
		return lazy;
	}

	/**
	 * Gets the maximum size of a slice of a dataset in a given dimension
	 * which should normally fit in memory. Note that it might be possible
	 * to get more in memory, this is a conservative estimate and seems to
	 * almost always work at the size returned; providing Xmx is less than
	 * the physical memory.
	 * 
	 * To get more in memory increase -Xmx setting or use an expression
	 * which calls a rolling function (like rmean) instead of slicing directly
	 * to memory.
	 * 
	 * @param lazySet
	 * @param dimension
	 * @return maximum size of dimension that can be sliced.
	 */
	public static int getMaxSliceLength(ILazyDataset lazySet, int dimension) {
		// size in bytes of each item
		final double size = AbstractDataset.getItemsize(AbstractDataset.getDTypeFromClass(lazySet.elementClass()), lazySet.getElementsPerItem());
		
		// Max in bytes takes into account our minimum requirement
		final double max  = Math.max(Runtime.getRuntime().totalMemory(), Runtime.getRuntime().maxMemory());
		
        // Firstly if the whole dataset it likely to fit in memory, then we allow it.
		// Space specified in bytes per item available
		final double space = max/lazySet.getSize();

		// If we have room for this whole dataset, then fine
		int[] shape = lazySet.getShape();
		if (space >= size)
			return shape[dimension];
		
		// Otherwise estimate what we can fit in, conservatively.
		// First get size of one slice, see it that fits, if not, still return 1
		double sizeOneSlice = size; // in bytes
		for (int dim = 0; dim < shape.length; dim++) {
			if (dim == dimension)
				continue;
			sizeOneSlice *= shape[dim];
		}
		double avail = max / sizeOneSlice;
		if (avail < 1)
			return 1;

		// We fudge this to leave some room
		return (int) Math.floor(avail/4d);
	}
}
