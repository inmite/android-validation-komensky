package eu.inmite.android.lib.validations.forms.validators;

import android.content.Context;

import java.lang.annotation.Annotation;

/**
 * Checks the given numerical string for a "banking modulo 11 validity".
 *
 * @author Tomas Vondracek
 */
public class CzechBankAccountNumberValidator extends BaseValidator<String[]> {

	private static final int[] PREFIX_WEIGHTS = {10, 5, 8, 4, 2, 1};
	private static final int[] NUMBER_WEIGHTS = {6, 3, 7, 9, 10, 5, 8, 4, 2, 1};

	@Override
	public boolean validate(Annotation annotation, String[] parts) {
		if (parts == null || parts.length < 1 || parts.length > 2) {
			return false;
		}

		String prefix = null;
		final String number;

		if (parts.length == 1) {
			number = parts[0];
		} else if (parts.length == 2) {
			prefix = parts[0];
			number = parts[1];
		} else {
			return false;
		}

		if (prefix != null) {
			boolean validPrefix = isModulo11(prefix, PREFIX_WEIGHTS);
			if (!validPrefix) {
				return false;
			}
		}

		if (number.length() > 10) {
			return false;
		}
		// check second part:
		return isModulo11(number, NUMBER_WEIGHTS);
	}

	private static boolean isModulo11(String number, int[] weights) {
		long sum = 0;
		final int unusedNumbers = weights.length - number.length();
		for (int k = number.length() - 1; k >= 0; k--) {
			int character = number.charAt(k) - '0';
			int numberWeight = weights[k + unusedNumbers];
			sum += character * numberWeight;
		}

		return (sum % 11) == 0;
	}

	@Override
	public String getMessage(Context context, Annotation annotation, String[] input) {
		return null;
	}
}
