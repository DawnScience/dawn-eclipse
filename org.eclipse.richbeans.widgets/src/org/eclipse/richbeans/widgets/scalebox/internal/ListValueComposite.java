/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.richbeans.widgets.scalebox.internal;

import org.eclipse.richbeans.widgets.internal.BeanUIWithoutOSGi;
import org.eclipse.richbeans.widgets.scalebox.ScaleBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Used by the RangeBox, not intended for exterior use.
 */
public class ListValueComposite extends Composite {
	private ScaleBox value;

	public ListValueComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label lblValue = new Label(this, SWT.NONE);
		lblValue.setText("Value");
		
		value = new ScaleBox(this, SWT.NONE);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		try {
			BeanUIWithoutOSGi.switchState(this, true);
		} catch (Exception ignored) {
			// We can carry on if this happens.
		}

	}
	public ScaleBox getValue() {
		return value;
	}
}
