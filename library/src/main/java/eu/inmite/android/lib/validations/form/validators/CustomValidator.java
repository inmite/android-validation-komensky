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

import eu.inmite.android.lib.validations.exception.FormsValidationException;
import eu.inmite.android.lib.validations.form.annotations.Custom;
import eu.inmite.android.lib.validations.form.annotations.Joined;
import eu.inmite.android.lib.validations.form.annotations.ValidatorFor;
import eu.inmite.android.lib.validations.form.iface.IValidator;

import java.lang.annotation.Annotation;

import android.content.Context;

/**
 * @author Tomas Vondracek
 */
@ValidatorFor({Joined.class, Custom.class})
public class CustomValidator extends BaseValidator<Object> {

	@Override
	public boolean validate(Annotation annotation, Object input) {
		if (annotation instanceof Joined) {
			try {
				IValidator validator = ((Joined) annotation).validator().newInstance();
				return validator.validate(annotation, input);
			} catch (InstantiationException e) {
				throw new FormsValidationException(e);
			} catch (IllegalAccessException e) {
				throw new FormsValidationException(e);
			}
		} else if (annotation instanceof Custom) {
			try {
				IValidator validator = ((Custom) annotation).value().newInstance();
				return validator.validate(annotation, input);
			} catch (InstantiationException e) {
				throw new FormsValidationException(e);
			} catch (IllegalAccessException e) {
				throw new FormsValidationException(e);
			}
		} else {
			throw new FormsValidationException("unknown annotation " + annotation);
		}
	}

	@Override
	public String getMessage(Context context, Annotation annotation, Object input) {
		if (annotation instanceof Joined) {
			try {
				IValidator validator = ((Joined) annotation).validator().newInstance();
				return validator.getMessage(context, annotation, input);
			} catch (InstantiationException e) {
				throw new FormsValidationException(e);
			} catch (IllegalAccessException e) {
				throw new FormsValidationException(e);
			}
		} else if (annotation instanceof Custom) {
			try {
				IValidator validator = ((Custom) annotation).value().newInstance();
				return validator.getMessage(context, annotation, input);
			} catch (InstantiationException e) {
				throw new FormsValidationException(e);
			} catch (IllegalAccessException e) {
				throw new FormsValidationException(e);
			}
		} else {
			throw new FormsValidationException("unknown annotation " + annotation);
		}
	}
}
