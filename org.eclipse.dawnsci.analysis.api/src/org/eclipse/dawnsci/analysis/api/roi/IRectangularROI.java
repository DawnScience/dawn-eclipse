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

package org.eclipse.dawnsci.analysis.api.roi;

public interface IRectangularROI extends IOrientableROI {
	@Override
	public IRectangularROI copy();

	/**
	 * @param i
	 * @return length in given dimension
	 */
	public double getLength(int i);

	/**
	 * End point of rectangle
	 * @return end
	 */
	public double[] getEndPoint();
}
