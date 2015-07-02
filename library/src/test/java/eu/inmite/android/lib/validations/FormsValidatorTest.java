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

package eu.inmite.android.lib.validations;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.MinValue;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.iface.IValidationCallback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

	@Test
	public void validInputShouldPass() throws Exception {
		final SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("10");
		model.editMessage.setText("0123456789");

		TestValidationCallback callback = new TestValidationCallback(0, 2);
		boolean result = FormValidator.validate(Robolectric.application, model, callback);
		assertTrue(callback.called);
		assertTrue(result);
	}

	@Test
	public void validInputShouldPassRepeated() throws Exception {
		SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("10");
		model.editMessage.setText("0123456789");

		boolean result = FormValidator.validate(Robolectric.application, model, null);
		assertTrue(result);

		TestValidationCallback callback = new TestValidationCallback(0, 2);
		result = FormValidator.validate(Robolectric.application, model, callback);
		assertTrue(result);
	}

	@Test
	public void emptyInputShouldNotPass() throws Exception {
		SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("");
		model.editMessage.setText(null);

		TestValidationCallback callback = new TestValidationCallback(2, 0);
		boolean result = FormValidator.validate(Robolectric.application, model, callback);
		assertFalse(result);
		assertTrue(callback.called);
	}

	@Test
	public void invalidMinValueInputShouldNotPass() throws Exception {
		SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("0");
		model.editMessage.setText("0123456789");

		TestValidationCallback callback = new TestValidationCallback(1, 1);
		boolean result = FormValidator.validate(Robolectric.application, model, callback);
		assertFalse(result);
	}

	@Test
	public void invalidMinLengthInputShouldNotPass() throws Exception {
		SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("2");
		model.editMessage.setText("00");

		TestValidationCallback callback = new TestValidationCallback(1, 1);
		boolean result = FormValidator.validate(Robolectric.application, model, callback);
		assertFalse(result);
	}

	@Test
	public void invalidValueOnInvisibleFieldShouldPass() {
		SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("");
		model.editMessage.setText("0123456789");
		
		model.txtAmount.setVisibility(View.GONE);

		TestValidationCallback callback = new TestValidationCallback(0, 1);
		boolean result = FormValidator.validate(Robolectric.application, model, callback);
		assertTrue(result);
	}

	@Test
	public void testTrimmedInput() {
		final SimpleModelUnderValidation model = new SimpleModelUnderValidation(Robolectric.application);
		model.txtAmount.setText("   ");
		model.editMessage.setText("0123456789");

		final boolean[] called = new boolean[1];
		boolean result = FormValidator.validate(Robolectric.application, model, new IValidationCallback() {
			@Override
			public void validationComplete(boolean result,
			                               List<FormValidator.ValidationFail> failedValidations,
			                               List<View> passedValidations) {
				called[0] = true;
				assertEquals(1, failedValidations.size());
				assertEquals(1, passedValidations.size());
				assertEquals(model.txtAmount, failedValidations.get(0).view);
			}
		});
		assertTrue(called[0]);
		assertFalse(result);
	}


	private static class TestValidationCallback implements IValidationCallback {
		private final int expectedSuccessCount;
		private int expectedFailedCount;
		boolean called;

		public TestValidationCallback(int expectedFailedCount, int expectedSuccessCount) {
			this.expectedFailedCount = expectedFailedCount;
			this.expectedSuccessCount = expectedSuccessCount;
		}

		@Override
		public void validationComplete(boolean result,
		                               List<FormValidator.ValidationFail> failedValidations,
		                               List<View> passedValidations) {
			called = true;
			assertEquals(expectedFailedCount, failedValidations.size());
			assertEquals(expectedSuccessCount, passedValidations.size());
		}
	}
}
