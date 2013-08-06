package eu.inmite.android.fw.validation.forms.callback;

import android.content.Context;
import android.widget.Toast;
import eu.inmite.android.fw.validation.forms.FormsValidator;

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
	protected void showValidationMessage(FormsValidator.ValidationFail firstFail) {
		Toast.makeText(mContext, firstFail.message, Toast.LENGTH_SHORT).show();
	}
}
