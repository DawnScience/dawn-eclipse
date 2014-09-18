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

package org.eclipse.dawnsci.analysis.dataset.roi;

import java.io.Serializable;

import org.eclipse.dawnsci.analysis.api.roi.IOrientableROI;

public class OrientableROIBase extends ROIBase implements IOrientableROI, Serializable {
	protected double ang;     // angle in radians
	protected double cang;
	protected double sang;

	@Override
	public OrientableROIBase copy() {
		OrientableROIBase c = new OrientableROIBase();
		c.name = name;
		c.spt = spt.clone();
		c.plot = plot;
		c.ang = ang;
		c.cang = cang;
		c.sang = sang;
		return c;
	}

	/**
	 * @return Returns the angle in degrees
	 */
	@Override
	public double getAngleDegrees() {
		return Math.toDegrees(ang);
	}

	/**
	 * @param angle The angle in degrees to set
	 */
	@Override
	public void setAngleDegrees(double angle) {
		setAngle(Math.toRadians(angle));
	}

	private final static double TWOPI = 2.0 * Math.PI;
	/**
	 * Make sure angle lie in permitted ranges:
	 *  0 <= ang < 2*pi
	 */
	protected void checkAngle() {
		while (ang < 0) {
			ang += TWOPI;
		}
		while (ang >= TWOPI) {
			ang -= TWOPI;
		}
		cang = Math.cos(ang);
		sang = Math.sin(ang);
	}

	/**
	 * @param rx 
	 * @param ry 
	 * @return array with original Cartesian coordinates
	 */
	protected double[] transformToOriginal(double rx, double ry) {
		double[] car = { rx * cang - ry * sang, rx * sang + ry * cang };
		return car;
	}

	/**
	 * @param rx 
	 * @return array with original Cartesian coordinates
	 */
	protected double[] transformXToOriginal(double rx) {
		double[] car = { rx * cang, rx * sang };
		return car;
	}

	/**
	 * @param ox 
	 * @param oy 
	 * @return array with rotated Cartesian coordinates
	 */
	protected double[] transformToRotated(double ox, double oy) {
		double[] car = { ox * cang + oy * sang, -ox * sang + oy * cang };
		return car;
	}

	/**
	 * @param angle
	 * @return angle between 0 and 2pi
	 */
	protected static double sanifyAngle(double angle) {
		while (angle < 0) {
			angle += TWOPI;
		}
		while (angle >= TWOPI) {
			angle -= TWOPI;
		}
		return angle;
	}

	/**
	 * @param angles
	 * @return angles between 0 and 2pi
	 */
	protected static double[] sanifyAngles(double... angles) {
		for (int i = 0; i < angles.length; i++) {
			angles[i] = sanifyAngle(angles[i]);
		}
		return angles;
	}

	/**
	 * @return Returns the angle
	 */
	@Override
	public double getAngle() {
		return ang;
	}

	/**
	 * @param angle The major axis angle to set
	 */
	@Override
	public void setAngle(double angle) {
		ang = angle;
		checkAngle();
		setDirty();
	}
}
