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

package eu.inmite.android.lib.validations.test;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import eu.inmite.android.lib.validations.forms.FormValidator;
import eu.inmite.android.lib.validations.forms.annotations.*;
import eu.inmite.android.lib.validations.forms.iface.ICondition;
import org.junit.Assert;
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
public class FormsValidatorWithConditionTest {

	public static final class PassingCondition implements ICondition<String> {
		@Override
		public boolean evaluate(String input) {
			return true;
		}
	}

	public static final class FailingCondition implements ICondition<String> {
		@Override
		public boolean evaluate(String input) {
			return false;
		}
	}

	private static class ModelWithPassingConditionValidation extends LinearLayout {

		@NotEmpty
		EditText editMessage;

		@NotEmpty
		@Condition(value = PassingCondition.class, validationAnnotation = NotEmpty.class, viewId = 10000)
		TextView txtNumber;

		private ModelWithPassingConditionValidation(Context context) {
			super(context);
			txtNumber = new TextView(context);
			txtNumber.setId(10000);
			editMessage = new EditText(context);
			editMessage.setId(20000);

			this.addView(txtNumber);
			this.addView(editMessage);
		}
	}
	private static class ModelWithFailingConditionValidation extends LinearLayout {

		@NotEmpty
		@Condition(value = FailingCondition.class, validationAnnotation = NotEmpty.class, viewId = 10000)
		TextView txtNumber;

		private ModelWithFailingConditionValidation(Context context) {
			super(context);
			txtNumber = new TextView(context);
			txtNumber.setId(10000);

			this.addView(txtNumber);
		}
	}


	@Test
	public void emptyInputShouldNotPass() throws Exception {
		ModelWithPassingConditionValidation model = new ModelWithPassingConditionValidation(Robolectric.application);
		model.txtNumber.setText("");
		model.editMessage.setText("");

		boolean result = FormValidator.validate(Robolectric.application, model, null);
		Assert.assertFalse(result);
	}

	@Test
	public void emptyInputShouldPassBecauseOfCondition() throws Exception {
		ModelWithFailingConditionValidation model = new ModelWithFailingConditionValidation(Robolectric.application);
		model.txtNumber.setText("");

		boolean result = FormValidator.validate(Robolectric.application, model, null);
		Assert.assertTrue("validate result should have been true, but it is " + result, result);
	}

}
