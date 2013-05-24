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
import android.text.TextUtils;
import eu.inmite.android.fw.validation.forms.Utils;
import eu.inmite.android.fw.validation.forms.annotations.*;
import eu.inmite.android.fw.validation.forms.iface.IValidator;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;

/**
 * @author Tomas Vondracek
 */
@ValidatorFor({MinNumberValue.class, MaxNumberValue.class})
public class NumberValueValidator implements IValidator<CharSequence> {

	@Override
	public boolean validate(Annotation annotation, CharSequence input) {
		if (TextUtils.isEmpty(input)) {
			return false;
		}
		BigDecimal value;
		try {
			Number parsedNumber = Utils.parseAmount(input.toString());
			value = new BigDecimal(parsedNumber.toString());
		} catch (Exception e) {
			return false;
		}

		if (annotation instanceof MinNumberValue) {
			ComparingPolicy policy = ((MinNumberValue) annotation).policy();
			BigDecimal minimum = new BigDecimal(((MinNumberValue) annotation).value());
			int comparison = value.compareTo(minimum);
			return policy == ComparingPolicy.EXCLUSIVE ?
					comparison > 0 : comparison >= 0;
		} else if (annotation instanceof MaxNumberValue) {
			ComparingPolicy policy = ((MaxNumberValue) annotation).policy();
			BigDecimal maximum = new BigDecimal(((MaxNumberValue) annotation).value());

			int comparison = value.compareTo(maximum);
			return policy == ComparingPolicy.EXCLUSIVE ?
					comparison < 0 : comparison <= 0;
		} else {
			throw new IllegalStateException("unknown annotation for ValueValidator " + annotation);
		}
	}

	@Override
	public String getMessage(Context context, Annotation annotation, CharSequence input) {
		String value = (String) AnnotationsHelper.getAnnotationValue(annotation);
		Integer messageId = (Integer) AnnotationsHelper.getAnnotationValueWithName(annotation, "messageId");

		String message = null;
		if (messageId != null && messageId > 0) {
			message = context.getString(messageId, value, input);
		}
		return message;
	}
}
