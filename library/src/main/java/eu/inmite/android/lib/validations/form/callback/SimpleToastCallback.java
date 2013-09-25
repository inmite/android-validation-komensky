package eu.inmite.android.lib.validations.form.callback;

import android.content.Context;
import android.widget.Toast;
import eu.inmite.android.lib.validations.form.FormValidator;

/**
 * Validation callback that will show toast for first validation fail
 *
 * @author Tomas Vondracek
 */
public class SimpleToastCallback extends SimpleCallback {

	public SimpleToastCallback(Context context) {
		this (context, false);
	}

	public SimpleToastCallback(Context context, boolean focusFirstFail) {
		super(context, focusFirstFail);
	}

	@Override
	protected void showValidationMessage(FormValidator.ValidationFail firstFail) {
		Toast.makeText(mContext, firstFail.message, Toast.LENGTH_SHORT).show();
	}
}
