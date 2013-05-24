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

package eu.inmite.android.fw.validation.forms.validators;

import android.content.Context;
import eu.inmite.android.fw.validation.forms.annotations.*;
import eu.inmite.android.fw.validation.forms.iface.IValidator;

import java.lang.annotation.Annotation;

/**
 * @author Tomas Vondracek
 */
@ValidatorFor({MinLength.class, MaxLength.class})
public class LengthValidator implements IValidator<CharSequence> {

	@Override
	public boolean validate(Annotation annotation, CharSequence input) {
		if (input == null) {
			return false;
		}
		long length = input.length();

		if (annotation instanceof MinLength) {
			ComparingPolicy policy = ((MinLength) annotation).policy();
			long minimum = ((MinLength) annotation).value();

			return policy == ComparingPolicy.EXCLUSIVE ?
					length > minimum : length >= minimum;
		} else if (annotation instanceof MaxLength) {
			ComparingPolicy policy = ((MaxLength) annotation).policy();
			long max = ((MaxLength) annotation).value();

			return policy == ComparingPolicy.EXCLUSIVE ?
					length < max : length <= max;
		} else {
			throw new IllegalStateException("unknown annotation " + annotation);
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
