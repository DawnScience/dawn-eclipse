package org.eclipse.richbeans.generator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.metawidget.inspector.InspectionResultConstants.NAME;

import org.eclipse.richbeans.api.generator.IGuiGeneratorService;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GuiGeneratorTest extends SWTTestBase {

	private IGuiGeneratorService guiGenerator;
	private TestBean testBean;
	private Composite metawidget;

	@Before
	public void setUp() throws Exception {
		testBean = new TestBean();
		testBean.setStringField("String field value");
		testBean.setUiReadOnlyStringField("UiReadOnly string field value");
		testBean.setIntField(5);

		guiGenerator = new GuiGeneratorService();
		metawidget = (Composite) guiGenerator.generateGui(testBean, shell);
	}

	@After
	public void tearDown() throws Exception {
		guiGenerator = null;
	}

	@Test
	public void testStringFieldIsText() throws Exception {
		Control control = getControl("stringField");
		assertThat(control, is(instanceOf(Text.class)));
	}

	@Test
	public void testStringFieldInitialValue() throws Exception {
		Control control = getControl("stringField");
		assertThat(((Text) control).getText(), is(equalTo(testBean.getStringField())));
	}

	@Test
	public void testStringFieldWithGetterOnlyIsLabel() throws Exception {
		Control control = getControl("stringFieldWithGetterOnly");
		assertThat(control, is(instanceOf(Label.class)));
	}

	@Test
	public void testStringFieldWithGetterOnlyValue() throws Exception {
		Control control = getControl("stringFieldWithGetterOnly");
		assertThat(((Label) control).getText(), is(equalTo(testBean.getStringFieldWithGetterOnly())));
	}

	@Test
	public void testUiReadOnlyStringFieldIsLabel() throws Exception {
		Control control = getControl("uiReadOnlyStringField");
		assertThat(((Label) control).getText(), is(equalTo(testBean.getUiReadOnlyStringField())));
	}

	@Test
	public void testUiReadOnlyStringFieldValue() throws Exception {
		Control control = getControl("uiReadOnlyStringField");
		assertThat(((Label) control).getText(), is(equalTo(testBean.getUiReadOnlyStringField())));
	}

	@Test
	public void testStringFieldDataBinding() throws Exception {
		String newValue = "New value";
		testBean.setStringField(newValue);
		Control control = getControl("stringField");
		assertThat(((Text) control).getText(), is(equalTo(newValue)));
	}

	@Test
	public void testIntFieldIsSpinner() throws Exception {
		Control control = getControl("intField");
		assertThat(control, is(instanceOf(Spinner.class)));
	}

	@Test
	public void testIntFieldInitialValue() throws Exception {
		Control control = getControl("intField");
		assertThat(((Spinner) control).getSelection(), is(equalTo(testBean.getIntField())));
	}

	@Test
	public void testIntFieldMinimumValue() throws Exception {
		Control control = getControl("intField");
		assertThat(((Spinner) control).getMinimum(), is(equalTo(Integer.valueOf(TestBean.INT_FIELD_MIN_VALUE_STRING))));
	}

	@Test
	public void testIntFieldMaximumValue() throws Exception {
		Control control = getControl("intField");
		assertThat(((Spinner) control).getMaximum(), is(equalTo(Integer.valueOf(TestBean.INT_FIELD_MAX_VALUE_STRING))));
	}

	private Control getControl(String name) {
		return getControl(metawidget, name);
	}

	private static Control getControl(Composite container, String name) {
		for (Control child : container.getChildren()) {
			// TODO investigate this - is it necessary to check for name == null, and does this code work for nested Metawidgets?
			if (child.getData(NAME) == null && child instanceof Composite) {
				Control control = getControl((Composite) child, name);
				if (control != null) {
					return control;
				}
			}
			if (name.equals(child.getData(NAME))) {
				return child;
			}
		}
		return null; // not found
	}
}
