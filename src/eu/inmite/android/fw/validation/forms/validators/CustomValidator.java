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

import eu.inmite.android.fw.validation.exception.FormsValidationException;
import eu.inmite.android.fw.validation.forms.annotations.Custom;
import eu.inmite.android.fw.validation.forms.annotations.Joined;
import eu.inmite.android.fw.validation.forms.annotations.ValidatorFor;
import eu.inmite.android.fw.validation.forms.iface.IValidator;

import java.lang.annotation.Annotation;

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

}
