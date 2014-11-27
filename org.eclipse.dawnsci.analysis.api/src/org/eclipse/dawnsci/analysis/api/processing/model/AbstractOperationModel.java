/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Peter Chang - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.dawnsci.analysis.api.processing.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Extend this class for your model to avoid having to implement the get and set manually.
 * Do not put non-POJO methods in your models, keep them vanilla.
 * 
 * BE WARNED the get and set are not especially fast - do not call them from big loops!
 * 
 * This class MUST define a no argument constructor with getters and setters.
 * 
 * NOTE You can currently have only two level of inheritance below this class so
 * 
 * class B extends A ...
 * class A extends AbstractOperationModel 
 * 
 * Would be ok; BUT class C extends B ...  IS NOT OK - TWO LEVELS ONLY ARE ALLOWED! :)
 * 
 */
public abstract class AbstractOperationModel implements IOperationModel {
	
	/**
	 * Tries to find the no-argument getter for this field, ignoring case
	 * so that camel case may be used in method names. This means that this
	 * method is not particularly fast, so avoid calling in big loops!
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	@Override
	public Object get(String name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Object val = get(getClass(), name);
		if (val==null) val = get(getClass().getSuperclass(), name);
		return val;
	}
	
	private Object get(Class<?> clazz, String name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if (clazz==null || clazz.equals(Object.class)) return null;
		
		final String getter = getGetterName(name).toLowerCase();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getName().toLowerCase().equals(getter)) {
				if (method.getParameterTypes().length<1) {
					method.setAccessible(true);
					return method.invoke(this);
				}
			}
		}
		
		final String isser  = getIsserName(name).toLowerCase();
		for (Method method : methods) {
			if (method.getName().toLowerCase().equals(isser)) {
				if (method.getParameterTypes().length<1) {
					method.setAccessible(true);
					return method.invoke(this);
				}
			}
		}
		return null;
	}

	@Override
	public boolean isModelField(String name) throws NoSuchFieldException, SecurityException {
		
		Field field = null;
		try {
		    field = getClass().getDeclaredField(name);
		} catch (Exception ne) {
			field = getClass().getSuperclass().getDeclaredField(name);
		}

		OperationModelField omf = field.getAnnotation(OperationModelField.class);

		
		final String getter = getGetterName(name).toLowerCase();
		Method[] methods = getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().toLowerCase().equals(getter)) {
				if (method.getParameterTypes().length<1) {
					return true;
				}
			}
		}
		
		final String isser  = getIsserName(name).toLowerCase();
		for (Method method : methods) {
			if (method.getName().toLowerCase().equals(isser)) {
				if (method.getParameterTypes().length<1) {
					return true;
				}
			}
		}

	    return false;
	}

	/**
	 * Set a field by name using reflection.
	 * @param name
	 * @return fields old value, or null
	 */
	@Override
	public Object set(String name, Object value)throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Object oldValue = get(name);
		
		final String setter = getSetterName(name).toLowerCase();
		Method[] methods = getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().toLowerCase().equals(setter)) {
				if (method.getParameterTypes().length==1) {
					method.setAccessible(true);
					method.invoke(this, value);
				}
			}
		}
		return oldValue;
	}

	
	private static String getSetterName(final String fieldName) {
		if (fieldName == null) return null;
		return getName("set", fieldName);
	}
	/**
	 * There must be a smarter way of doing this i.e. a JDK method I cannot find. However it is one line of Java so
	 * after spending some time looking have coded self.
	 * 
	 * @param fieldName
	 * @return String
	 */
	private static String getGetterName(final String fieldName) {
		if (fieldName == null) return null;
		return getName("get", fieldName);
	}
	
	private static String getIsserName(final String fieldName) {
		if (fieldName == null)
			return null;
		return getName("is", fieldName);
	}
	private static String getName(final String prefix, final String fieldName) {
		return prefix + getFieldWithUpperCaseFirstLetter(fieldName);
	}
	public static String getFieldWithUpperCaseFirstLetter(final String fieldName) {
		return fieldName.substring(0, 1).toUpperCase(Locale.US) + fieldName.substring(1);
	}

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName,
				listener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,
				newValue);
	}

	/**
	 * Get a collection of the fields of the model that should be edited in the User interface
	 * for editing the model.
	 * 
	 * @return collection of fields.
	 * @throws Exception
	 */
	public Collection<ModelField> getModelFields() throws Exception {
		
		// Decided not to use the obvious BeanMap here because class problems with
		// GDA and we have to read annotations anyway.
		final List<Field> allFields = new ArrayList<Field>(31);
		allFields.addAll(Arrays.asList(getClass().getDeclaredFields()));
		allFields.addAll(Arrays.asList(getClass().getSuperclass().getDeclaredFields()));
		
		// The returned descriptor
		final List<ModelField> ret = new ArrayList<ModelField>();
		
		// fields
		for (Field field : allFields) {
			
			// If there is a getter/isser for the field we assume it is a model field.
			try {
				if (isModelField(field.getName())) {			
					ret.add(new ModelField(this, field.getName()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
		Collections.sort(ret, new Comparator<ModelField>() {
			@Override
			public int compare(ModelField o1, ModelField o2) {
				return o1.getDisplayName().toLowerCase().compareTo(o2.getDisplayName().toLowerCase());
			}
		});
		
		return ret;
	}

}
