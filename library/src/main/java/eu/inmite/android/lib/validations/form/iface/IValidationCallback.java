package eu.inmite.android.lib.validations.form.iface;

import eu.inmite.android.lib.validations.form.FormValidator;

import java.util.List;

/**
* @author Tomas Vondracek
*/
public interface IValidationCallback {

	/**
	 * Validation completed callback.
	 *
	 * @param result true if validation passed
	 * @param failedValidations collections of all failed validations, this collection should never be null and is immutable
	 */
	void validationComplete(boolean result, List<FormValidator.ValidationFail> failedValidations);
}
