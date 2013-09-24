package eu.inmite.android.lib.validations.forms.iface;

import eu.inmite.android.lib.validations.forms.FormValidator;

import java.util.List;

/**
* @author Tomas Vondracek
*/
public interface IValidationCallback {

	/**
	 * Validation completed callback.
	 *
	 * @param result true if validation passed
	 * @param failedValidations collections of all failed validations, this collection should never be null
	 */
	void validationComplete(boolean result, List<FormValidator.ValidationFail> failedValidations);
}
