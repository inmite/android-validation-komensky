package eu.inmite.android.fw.validation.test;

import android.content.Context;
import android.view.View;
import eu.inmite.android.fw.validation.forms.FormsValidator;
import eu.inmite.android.fw.validation.forms.annotations.MinLength;
import eu.inmite.android.fw.validation.forms.annotations.NotEmpty;
import eu.inmite.android.fw.validation.forms.iface.IFieldAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tomas Vondracek
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class CustomAdapterValidationTest {

	private static class CustomView extends View {

		private String customText = "";

		public CustomView(Context context) {
			super(context);
		}

		private String getCustomText() {
			return customText;
		}

		private void setCustomText(String customText) {
			this.customText = customText;
		}
	}

	public static class CustomViewAdapter implements IFieldAdapter<CustomView, String> {

		@Override
		public String getFieldValue(Annotation annotation, Object target, CustomView fieldView) {
			return fieldView.getCustomText();
		}
	}

	private static class ModelWithValidation {

		@NotEmpty
		@MinLength(3)
		private final CustomView view = new CustomView(Robolectric.application);
	}

	@Before
	public void setUp() throws Exception {
		FormsValidator.registerViewAdapter(CustomView.class, CustomViewAdapter.class);
	}

	@After
	public void tearDown() throws Exception {
		FormsValidator.clearViewAdapters();
	}

	@Test
	public void validInputShouldPass() throws Exception {
		ModelWithValidation model = new ModelWithValidation();
		model.view.setCustomText("123");

		final boolean valid = FormsValidator.validate(Robolectric.application, model, null);
		assertTrue(valid);
	}

	@Test
	public void invalidInputShouldNotPass() throws Exception {
		ModelWithValidation model = new ModelWithValidation();
		model.view.setCustomText("");

		final boolean valid = FormsValidator.validate(Robolectric.application, model, null);
		assertFalse(valid);
	}
}
