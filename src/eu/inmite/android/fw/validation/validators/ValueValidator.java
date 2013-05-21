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

package eu.inmite.android.fw.validation.validators;

import android.content.Context;
import android.text.TextUtils;
import eu.inmite.android.fw.validation.annotations.*;
import eu.inmite.android.fw.validation.annotations.ComparingPolicy;
import eu.inmite.android.fw.validation.annotations.MaxValue;
import eu.inmite.android.fw.validation.annotations.MinValue;
import eu.inmite.android.fw.validation.annotations.ValidatorFor;
import eu.inmite.android.fw.validation.iface.IValidator;

import java.lang.annotation.Annotation;

/**
 * @author Tomas Vondracek
 */
@ValidatorFor({MinValue.class, MaxValue.class})
public class ValueValidator implements IValidator<CharSequence> {

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

	@Override
	public String getMessage(Context context, Annotation annotation, CharSequence input) {
		Object value = AnnotationsHelper.getAnnotationValue(annotation);
		Integer messageId = (Integer) AnnotationsHelper.getAnnotationValueWithName(annotation, "messageId");

		String message = null;
		if (messageId != null && messageId > 0) {
			message = context.getString(messageId, value, input);
		}
		return message;
	}
}
