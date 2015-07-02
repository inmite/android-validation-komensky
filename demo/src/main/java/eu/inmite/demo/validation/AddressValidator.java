package eu.inmite.demo.validation;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.validators.BaseValidator;

/**
 * @author Tomáš Vondráček (vondracek@avast.com) on 02/07/15.
 */
public class AddressValidator extends BaseValidator<String[]> {

	@Override
	public boolean validate(Annotation annotation, String[] input) {
		for (String addressPart : input) {
			if (addressPart == null || addressPart.trim().length() == 0) {
				return false;
			}
		}
		return true;
	}
}
