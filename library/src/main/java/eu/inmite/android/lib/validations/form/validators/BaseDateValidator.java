package eu.inmite.android.lib.validations.form.validators;

import android.content.Context;
import eu.inmite.android.lib.validations.form.annotations.AnnotationsHelper;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Tomas Vondracek
 */
public abstract class BaseDateValidator extends BaseValidator<String> {

	protected abstract DateFormat getDateFormat(Annotation annotation);

	protected abstract boolean validateDate(Calendar cal);

	@Override
	public boolean validate(Annotation annotation, String input) {
		final DateFormat dateFormat = getDateFormat(annotation);
		try {
			Date date = dateFormat.parse(input);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			return validateDate(cal);
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
