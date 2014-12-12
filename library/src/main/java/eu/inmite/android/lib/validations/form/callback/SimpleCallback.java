package eu.inmite.android.lib.validations.form.callback;

import android.content.Context;
import android.view.View;

import java.util.List;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.iface.IValidationCallback;

/**
 * @author Tomas Vondracek
 */
public abstract class SimpleCallback implements IValidationCallback {

	protected final Context mContext;
	protected final boolean mFocusFirstFail;

	public SimpleCallback(Context context, boolean focusFirstFail) {
		mFocusFirstFail = focusFirstFail;
		mContext = context;
	}

    @Override
    public void validationComplete(boolean result, List<FormValidator.ValidationFail> failedValidations, List<View> passedValidations) {
		if (failedValidations.size() > 0) {
			FormValidator.ValidationFail firstFail = failedValidations.get(0);
			if (mFocusFirstFail) {
				firstFail.view.requestFocus();
			}
			showValidationMessage(firstFail);
		}
	}

	/**
	 * present validation message to the user
	 */
	protected abstract void showValidationMessage(FormValidator.ValidationFail firstFail);
}
