/*
 * Copyright (c) 2013, Inmite s.r.o. (www.inmite.eu).
 *
 * All rights reserved. This source code can be used only for purposes specified
 * by the given license contract signed by the rightful deputy of Inmite s.r.o.
 * This source code can be used only by the owner of the license.
 *
 * Any disputes arising in respect of this agreement (license) shall be brought
 * before the Municipal Court of Prague.
 */

package eu.inmite.android.fw.validation.test;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import eu.inmite.android.fw.validation.forms.FormsValidator;
import eu.inmite.android.fw.validation.forms.annotations.Joined;
import eu.inmite.android.fw.validation.forms.annotations.MinLength;
import eu.inmite.android.fw.validation.forms.annotations.MinValue;
import eu.inmite.android.fw.validation.forms.annotations.NotEmpty;
import eu.inmite.android.fw.validation.forms.validators.CzechBankAccountNumberValidator;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * @author Tomas Vondracek
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class FormsValidatorTest {

	private static class SimpleModelUnderValidation {

		@NotEmpty
		@MinValue(1)
		TextView txtAmount;

		@NotEmpty
		@MinLength(10)
		EditText editMessage;

		private SimpleModelUnderValidation(Context context) {
			txtAmount = new TextView(context);
			editMessage = new EditText(context);
		}
	}

	private static class ModelWithJoinedUnderValidation extends LinearLayout {

		@NotEmpty
		EditText editPrefix;

		@NotEmpty
		@Joined(value = {10000, 20000}, validator = CzechBankAccountNumberValidator.class)
		TextView txtNumber;

		private ModelWithJoinedUnderValidation(Context context) {
			super(context);
			txtNumber = new TextView(context);
			txtNumber.setId(10000);
			editPrefix = new EditText(context);
			editPrefix.setId(20000);

			this.addView(txtNumber);
			this.addView(editPrefix);
		}
	}

	@Test
	public void validInputShouldPass() throws Exception {
		SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("10");
		model.editMessage.setText("0123456789");

		boolean result = FormsValidator.validate(Robolectric.application, model, null);
		Assert.assertTrue(result);
	}

	@Test
	public void validInputShouldPassRepeated() throws Exception {
		SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("10");
		model.editMessage.setText("0123456789");

		boolean result = FormsValidator.validate(Robolectric.application, model, null);
		Assert.assertTrue(result);
		result = FormsValidator.validate(Robolectric.application, model, null);
		Assert.assertTrue(result);
	}

	@Test
	public void emptyInputShouldNotPass() throws Exception {
		SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("");
		model.editMessage.setText(null);

		boolean result = FormsValidator.validate(Robolectric.application, model, null);
		Assert.assertFalse(result);
	}

	@Test
	public void invalidMinValueInputShouldNotPass() throws Exception {
		SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("0");
		model.editMessage.setText("0123456789");

		boolean result = FormsValidator.validate(Robolectric.application, model, null);
		Assert.assertFalse(result);
	}

	@Test
	public void invalidMinLengthInputShouldNotPass() throws Exception {
		SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("2");
		model.editMessage.setText("00");

		boolean result = FormsValidator.validate(Robolectric.application, model, null);
		Assert.assertFalse(result);
	}

	@Test
	public void combineValidationShouldPass() {
		ModelWithJoinedUnderValidation model = new ModelWithJoinedUnderValidation(Robolectric.application);
		model.txtNumber.setText("123");
		model.editPrefix.setText("0");

		boolean result = FormsValidator.validate(Robolectric.application, model, null);
		Assert.assertTrue(result);
	}

	@Test
	public void combineValidationShouldFailOnModulo() {
		ModelWithJoinedUnderValidation model = new ModelWithJoinedUnderValidation(Robolectric.application);
		model.txtNumber.setText("1234");
		model.editPrefix.setText("0");

		boolean result = FormsValidator.validate(Robolectric.application, model, null);
		Assert.assertFalse(result);
	}

	@Test
	public void cacheShouldBeEmptyAfterGC() {
		ModelWithJoinedUnderValidation model = new ModelWithJoinedUnderValidation(Robolectric.application);
		model.txtNumber.setText("1234");
		model.editPrefix.setText("0");
		FormsValidator.validate(Robolectric.application, model, null);

		model = null;
		System.gc();    // target should be collected and cache should be empty
		Robolectric.runBackgroundTasks();
		Robolectric.runUiThreadTasks();
		Robolectric.runUiThreadTasksIncludingDelayedTasks();
		// TODO not sure about stability of this test
		final boolean wasntEmpty = FormsValidator.clearCaches();

		Assert.assertFalse(wasntEmpty);
	}
}
