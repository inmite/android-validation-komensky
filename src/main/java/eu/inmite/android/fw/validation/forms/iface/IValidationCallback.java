package eu.inmite.android.fw.validation.forms.iface;

import eu.inmite.android.fw.validation.forms.FormsValidator;

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
	void validationComplete(boolean result, List<FormsValidator.ValidationFail> failedValidations);
}
