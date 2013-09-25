/*
 * Copyright (c) 2013, Inmite s.r.o. (www.inmite.eu).
 *
 * All rights reserved. This source code can be used only for purposes specified
 * by the given license contract signed by the rightful deputy of Inmite s.r.o.
 * This source code can be used only by the owner of the license.
 *
 * Any disputes arising in respect of this agreement (license) shall be brought
 * before the Municipal Court of Prague.
 */

package eu.inmite.android.lib.validations.form.validators;

import android.text.TextUtils;
import eu.inmite.android.lib.validations.form.Utils;
import eu.inmite.android.lib.validations.form.annotations.ComparingPolicy;
import eu.inmite.android.lib.validations.form.annotations.MaxNumberValue;
import eu.inmite.android.lib.validations.form.annotations.MinNumberValue;
import eu.inmite.android.lib.validations.form.annotations.ValidatorFor;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;

/**
 * @author Tomas Vondracek
 */
@ValidatorFor({MinNumberValue.class, MaxNumberValue.class})
public class NumberValueValidator extends BaseValidator<CharSequence> {

	@Override
	public boolean validate(Annotation annotation, CharSequence input) {
		if (TextUtils.isEmpty(input)) {
			return false;
		}
		final BigDecimal value;
		try {
			Number parsedNumber = Utils.parseAmount(input.toString());
			value = new BigDecimal(parsedNumber.toString());
		} catch (Exception e) {
			return false;
		}

		if (annotation instanceof MinNumberValue) {
			return validateMinimum((MinNumberValue) annotation, value);
		} else if (annotation instanceof MaxNumberValue) {
			return validateMaximum((MaxNumberValue) annotation, value);
		} else {
			throw new IllegalStateException("unknown annotation for NumberValueValidator " + annotation);
		}
	}

	private boolean validateMinimum(MinNumberValue annotation, BigDecimal value) {
		final ComparingPolicy policy = annotation.policy();
		final BigDecimal minimum = new BigDecimal(annotation.value());
		final int comparison = value.compareTo(minimum);

		return policy == ComparingPolicy.EXCLUSIVE ?
				comparison > 0 : comparison >= 0;
	}

	private boolean validateMaximum(MaxNumberValue annotation, BigDecimal value) {
		final ComparingPolicy policy = annotation.policy();
		final BigDecimal maximum = new BigDecimal(annotation.value());

		final int comparison = value.compareTo(maximum);

		return policy == ComparingPolicy.EXCLUSIVE ?
				comparison < 0 : comparison <= 0;
	}
}
