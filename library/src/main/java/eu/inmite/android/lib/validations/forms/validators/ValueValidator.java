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

package eu.inmite.android.lib.validations.forms.validators;

import android.text.TextUtils;
import eu.inmite.android.lib.validations.forms.annotations.ComparingPolicy;
import eu.inmite.android.lib.validations.forms.annotations.MaxValue;
import eu.inmite.android.lib.validations.forms.annotations.MinValue;
import eu.inmite.android.lib.validations.forms.annotations.ValidatorFor;

import java.lang.annotation.Annotation;

/**
 * @author Tomas Vondracek
 */
@ValidatorFor({MinValue.class, MaxValue.class})
public class ValueValidator extends BaseValidator<CharSequence> {

	@Override
	public boolean validate(Annotation annotation, CharSequence input) {
		if (TextUtils.isEmpty(input)) {
			return false;
		}

		final long value;
		try {
			value = Long.parseLong(input.toString());
		} catch (NumberFormatException e) {
			return false;
		}
		if (annotation instanceof MinValue) {
			ComparingPolicy policy = ((MinValue) annotation).policy();
			long minimum = ((MinValue) annotation).value();

			return policy == ComparingPolicy.EXCLUSIVE ?
					value > minimum : value >= minimum;
		} else if (annotation instanceof MaxValue) {
			ComparingPolicy policy = ((MaxValue) annotation).policy();
			long maximum = ((MaxValue) annotation).value();

			return policy == ComparingPolicy.EXCLUSIVE ?
					value < maximum : value <= maximum;
		} else {
			throw new IllegalStateException("unknown annotation for ValueValidator " + annotation);
		}
	}
}
