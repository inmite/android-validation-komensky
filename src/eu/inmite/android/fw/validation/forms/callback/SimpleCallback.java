package eu.inmite.android.fw.validation.forms.callback;

import android.content.Context;
import eu.inmite.android.fw.validation.forms.FormValidator;
import eu.inmite.android.fw.validation.forms.iface.IValidationCallback;

import java.util.List;

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
	public void validationComplete(boolean result, List<FormValidator.ValidationFail> failedValidations) {
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
