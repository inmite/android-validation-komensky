package eu.inmite.android.fw.validation.forms.annotations;

import java.lang.annotation.Annotation;

/**
 * Since annotations don't support inheritance, we use convention and this helper get values from annotation by convention.
 * @author Tomas Vondracek
 */
public class AnnotationsHelper {

	public static final String VALUE_NAME = "value";

	public static Object getAnnotationValue(Annotation annotation) {
		return getAnnotationValueWithName(annotation, VALUE_NAME);
	}

	public static Object getAnnotationValueWithName(Annotation annotation, String valueName) {
		try {
			return annotation.annotationType().getMethod(valueName).invoke(annotation);
		} catch (Exception e) {
			return null;
		}
	}
}
