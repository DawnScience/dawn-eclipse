/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.richbeans.widgets.wrappers;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class ComboWrapperWithGetCombo extends ComboWrapper {

	public ComboWrapperWithGetCombo(Composite parent, int style) {
		super(parent, style);
	}
	
	public Combo getCombo(){
		return this.combo;
	}

}