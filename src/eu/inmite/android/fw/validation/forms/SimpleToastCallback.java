package eu.inmite.android.fw.validation.forms;

import android.content.Context;
import android.widget.Toast;
import eu.inmite.android.fw.validation.forms.iface.IValidationCallback;

import java.util.List;

/**
 * Validation callback that will show toast for first validation fail
 *
 * @author Tomas Vondracek
 */
public class SimpleToastCallback implements IValidationCallback {

	private final Context mContext;
	private final boolean mFocusFirstFail;

	public SimpleToastCallback(Context context) {
		this (context, false);
	}

	public SimpleToastCallback(Context context, boolean focusFirstFail) {
		mContext = context;
		mFocusFirstFail = focusFirstFail;
	}

	@Override
	public void validationComplete(boolean result, List<FormsValidator.ValidationFail> failedValidations) {
		if (failedValidations.size() > 0) {
			FormsValidator.ValidationFail firstFail = failedValidations.get(0);
			if (mFocusFirstFail) {
				firstFail.view.requestFocus();
			}
			Toast.makeText(mContext, firstFail.message, Toast.LENGTH_SHORT).show();
		}
	}
}
