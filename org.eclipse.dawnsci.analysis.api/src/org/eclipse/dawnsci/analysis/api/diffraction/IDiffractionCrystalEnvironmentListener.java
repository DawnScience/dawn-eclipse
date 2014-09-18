/*-
 * Copyright (c) 2013 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.api.diffraction;

import java.util.EventListener;

public interface IDiffractionCrystalEnvironmentListener extends EventListener {

	/**
	 * Fired when diffraction crystal environment changes.
	 * @param evt
	 */
	public void diffractionCrystalEnvironmentChanged(DiffractionCrystalEnvironmentEvent evt);
}
