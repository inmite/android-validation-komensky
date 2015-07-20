package eu.inmite.android.lib.validations;

import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.iface.IFieldAdapter;

import static org.junit.Assert.assertTrue;

/**
 * @author Tomáš Vondráček (vondracek@avast.com) on 20/07/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class CustomAdapterForSupportedViewTest {

	public static class CustomTextViewAdapter implements IFieldAdapter<TextView, CharSequence> {

		@Override
		public String getFieldValue(Annotation annotation, TextView fieldView) {
			return fieldView.getText() + "some added text";
		}
	}

	private static class ModelWithValidation {

		@NotEmpty
		@MinLength(3)
		private final TextView view = new TextView(Robolectric.application);
	}

	@Before
	public void setUp() throws Exception {
		FormValidator.registerViewAdapter(TextView.class, CustomTextViewAdapter.class);
	}

	@After
	public void tearDown() throws Exception {
		FormValidator.clearViewAdapters();
	}

	@Test
	public void customAdapterShouldBeUsed() throws Exception {
		final ModelWithValidation model = new ModelWithValidation();
		model.view.setText(""); //adapter will add text and therefore validation should pass

		final boolean valid = FormValidator.validate(Robolectric.application, model, null);
		assertTrue(valid);
	}
}
