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
import eu.inmite.android.lib.validations.form.annotations.DateNoWeekend;
import eu.inmite.android.lib.validations.form.annotations.ValidatorFor;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Tomas Vondracek
 */
@ValidatorFor(DateNoWeekend.class)
public class WeekendDateValidator extends BaseDateValidator {

	@Override
	protected DateFormat getDateFormat(final Annotation annotation) {
		DateNoWeekend dateAnnotation = (DateNoWeekend) annotation;
		final DateFormat dateFormat;
		if (TextUtils.isEmpty(dateAnnotation.datePattern())) {
			dateFormat = DateFormat.getDateInstance(dateAnnotation.dateStyle());
		} else {
			dateFormat = new SimpleDateFormat(dateAnnotation.datePattern());
		}
		return dateFormat;
	}

	@Override
	protected boolean validateDate(final Calendar cal, final Annotation annotation) {
		final int day = cal.get(Calendar.DAY_OF_WEEK);
		return !(day == Calendar.SUNDAY || day == Calendar.SATURDAY);
	}

}
