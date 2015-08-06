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
package org.eclipse.richbeans.widgets.decorator;

import java.text.DecimalFormat;

import org.eclipse.swt.widgets.Text;

public class IntegerDecorator extends BoundsDecorator {

	private boolean requireIntegers=false;
	public IntegerDecorator(Text text) {
		super(text, "[-0-9∞]+", new DecimalFormat("##########0"));
	}
	
	@Override
	public Number getValue() {
		Number val = parseValue(text.getText());
		return isRequireIntegers() ? val.intValue() : val;
	}
	
	@Override
	protected void fireValueChangedListeners(final ValueChangeEvent evt) {
        if (isRequireIntegers() && evt.getValue() instanceof Double) evt.setValue(evt.getValue().intValue());
        super.fireValueChangedListeners(evt);
	}
	
	public boolean isRequireIntegers() {
		return requireIntegers;
	}

	public void setRequireIntegers(boolean requireIntegers) {
		this.requireIntegers = requireIntegers;
	}


}
