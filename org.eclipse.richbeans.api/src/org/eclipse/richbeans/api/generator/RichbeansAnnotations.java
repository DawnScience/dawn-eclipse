package org.eclipse.richbeans.api.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations for use with the Richbeans decorated widgets.
 * 
 * @author James Mudd
 */
public class RichbeansAnnotations {

	/**
	 * Specify the minimum value a numeric field should contain.
	 * 
	 * @author James Mudd
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface MinimumValue {
		String value();
	}

	/**
	 * Specify the maximum value a numeric field should contain.
	 * 
	 * @author James Mudd
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface MaximumValue {
		String value();
	}
	
	/**
	 * Specify the units of a numeric field (e.g mm or eV).
	 * 
	 * @author James Mudd
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Units {
		String value();
	}

}
