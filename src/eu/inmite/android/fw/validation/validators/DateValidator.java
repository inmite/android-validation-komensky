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
import eu.inmite.android.fw.validation.annotations.AnnotationsHelper;
import eu.inmite.android.fw.validation.annotations.DateNoWeekend;
import eu.inmite.android.fw.validation.annotations.ValidatorFor;
import eu.inmite.android.fw.validation.iface.IValidator;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Tomas Vondracek
 */
@ValidatorFor(DateNoWeekend.class)
public class DateValidator implements IValidator<String> {

	@Override
	public boolean validate(Annotation annotation, String input) {
		DateNoWeekend dateAnnotation = (DateNoWeekend) annotation;
		final DateFormat dateFormat;
		if (TextUtils.isEmpty(dateAnnotation.datePattern())) {
			dateFormat = DateFormat.getDateInstance(dateAnnotation.dateStyle());
		} else {
			dateFormat = new SimpleDateFormat(dateAnnotation.datePattern());
		}
		try {
			Date date = dateFormat.parse(input);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (day == Calendar.SUNDAY|| day == Calendar.SATURDAY) {
				return false;
			}
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	@Override
	public String getMessage(Context context, Annotation annotation, String input) {
		Integer messageId = (Integer) AnnotationsHelper.getAnnotationValueWithName(annotation, "messageId");

		String message = null;
		if (messageId != null && messageId > 0) {
			message = context.getString(messageId, input);
		}
		return message;
	}
}
