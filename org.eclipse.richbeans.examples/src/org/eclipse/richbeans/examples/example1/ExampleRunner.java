package org.eclipse.richbeans.examples.example1;

import org.eclipse.richbeans.api.reflection.IBeanController;
import org.eclipse.richbeans.examples.ExampleJSONWritingValueListener;
import org.eclipse.richbeans.examples.example1.data.SimpleBean;
import org.eclipse.richbeans.examples.example1.ui.SimpleComposite;
import org.eclipse.richbeans.reflection.BeanService;
import org.eclipse.richbeans.widgets.util.SWTUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * An example of using the composite and bean together.
 * 
 * @author Matthew Gerring
 */
public class ExampleRunner {

	public static void main(String[] args) throws Exception {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
		shell.setText("Change a value to see bean as JSON");

		// Composite
		final SimpleComposite ui = new SimpleComposite(shell, SWT.NONE);
		ui.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Something to show value.
		final Label value = new Label(shell, SWT.WRAP);

		shell.pack();
		shell.setSize(420, 400);

		// Set some initial values
		final SimpleBean bean = new SimpleBean();
		bean.setX(10.0);
		bean.setY(5);

		// Connect the UI and bean
		final IBeanController controller = BeanService.getInstance().createController(ui, bean);
		controller.addValueListener(new ExampleJSONWritingValueListener(controller, value));
		controller.beanToUI();
		controller.switchState(true);

		SWTUtils.showCenteredShell(shell);
	}
}