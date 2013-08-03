package eu.inmite.android.fw.validation.forms.validators;

import android.content.Context;
import eu.inmite.android.fw.validation.forms.annotations.AnnotationsHelper;
import eu.inmite.android.fw.validation.forms.iface.IValidator;

import java.lang.annotation.Annotation;

/**
 * @author Tomas Vondracek
 */
public abstract class BaseValidator<T> implements IValidator<T> {

	@Override
	public String getMessage(Context context, Annotation annotation, T input) {
		Object value = AnnotationsHelper.getAnnotationValue(annotation);
		Integer messageId = (Integer) AnnotationsHelper.getAnnotationValueWithName(annotation, "messageId");

		String message = null;
		if (messageId != null && messageId > 0) {
			message = context.getString(messageId, value, input);
		}
		return message;
	}

	@Override
	public int getOrder(Annotation annotation) {
		Integer order = (Integer) AnnotationsHelper.getAnnotationValueWithName(annotation, "order");
		if (order != null) {
			return order;
		}
		return 0;
	}
}
