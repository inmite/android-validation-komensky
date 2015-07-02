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

import android.content.Context;
import android.text.TextUtils;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.ValidatorFor;

import java.lang.annotation.Annotation;

/**
 * @author Tomas Vondracek
 */
@ValidatorFor(NotEmpty.class)
public class NotEmptyValidator extends BaseValidator<CharSequence> {

	@Override
	public boolean validate(Annotation annotation, CharSequence input) {
		final CharSequence inputToValidate;
		if (((NotEmpty) annotation).trim()) {
			inputToValidate = input.toString().trim();
		} else {
			inputToValidate = input;
		}
		return ! TextUtils.isEmpty(inputToValidate);
	}

	@Override
	public String getMessage(Context context, Annotation annotation, CharSequence input) {
		final int messageId = ((NotEmpty) annotation).messageId();
		String message = null;
		if (messageId > 0) {
			message = context.getString(messageId, input);
		}
		return message;
	}
}
